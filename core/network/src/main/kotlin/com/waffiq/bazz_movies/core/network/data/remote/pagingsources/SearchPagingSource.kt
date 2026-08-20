package com.waffiq.bazz_movies.core.network.data.remote.pagingsources

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.common.value
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.SearchApiService
import com.waffiq.bazz_movies.core.network.utils.mappers.SearchMapper.withMediaType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
  private val searchApiService: SearchApiService,
  private val query: String,
  private val filters: Set<MediaType>,
) : PagingSource<Int, MultiSearchResponseItem>() {

  // tracks totalPages per type once known, so exhausted types can be skipped on later pages
  private val totalPagesByType = mutableMapOf<MediaType, Int>()

  override fun getRefreshKey(state: PagingState<Int, MultiSearchResponseItem>): Int? =
    state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MultiSearchResponseItem> {
    val position = params.key ?: INITIAL_PAGE_INDEX

    // only keep types that not yet fetched, or still have more pages left
    val activeTypes = filters.filter { type ->
      val known = totalPagesByType[type]
      known == null || position <= known
    }

    return try {
      // fetch all active types concurrently for this page
      val pages = coroutineScope {
        activeTypes.map { type -> async { type to fetchTypedPage(type, position) } }.awaitAll()
      }

      val items = mutableListOf<MultiSearchResponseItem>()
      var anyHasNext = false

      pages.forEach { (type, typedPage) ->

        // remember for next load's filtering
        totalPagesByType[type] = typedPage.totalPages
        items += typedPage.items

        // at least one type has more pages
        if (position < typedPage.totalPages) anyHasNext = true
      }

      LoadResult.Page(
        data = items,
        prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
        nextKey = if (anyHasNext) position + 1 else null,
      )
    } catch (exception: IOException) {
      LoadResult.Error(exception)
    } catch (exception: HttpException) {
      LoadResult.Error(exception)
    }
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal suspend fun fetchTypedPage(type: MediaType, page: Int): TypedSearchPage {
    val response = when (type) {
      MediaType.MULTI -> searchApiService.searchMulti(query, page)
      MediaType.MOVIE -> searchApiService.searchMovies(query, page)
      MediaType.TV -> searchApiService.searchTv(query, page)
      MediaType.PERSON -> searchApiService.searchPerson(query, page)
    }

    val results = response.results ?: throw IOException("Response data is null")

    // multi-search results already have mediaType
    // single-type endpoints don't, so need set mediaType for each of them
    val items = if (type == MediaType.MULTI) {
      results
    } else {
      results.map { it.withMediaType(type.value) }
    }
    return TypedSearchPage(items, response.totalPages ?: page)
  }

  internal data class TypedSearchPage(
    val items: List<MultiSearchResponseItem>,
    val totalPages: Int,
  )

  private companion object {
    const val INITIAL_PAGE_INDEX = 1
  }
}
