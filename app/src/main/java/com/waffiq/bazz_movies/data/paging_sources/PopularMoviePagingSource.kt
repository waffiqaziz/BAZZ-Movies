package com.waffiq.bazz_movies.data.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffiq.bazz_movies.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.utils.common.Constants.INITIAL_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class PopularMoviePagingSource(private val apiService: TMDBApiService) : PagingSource<Int, ResultItemResponse>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultItemResponse> {
    return try {
      val position = params.key ?: INITIAL_PAGE_INDEX
      val responseData = apiService.getPopularMovies(position).results

      LoadResult.Page(
        data = responseData,
        prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
        nextKey = if (responseData.isEmpty()) null else position + 1
      )
    } catch (exception: IOException) {
      LoadResult.Error(exception)
    } catch (exception: HttpException) {
      LoadResult.Error(exception)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, ResultItemResponse>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }
}