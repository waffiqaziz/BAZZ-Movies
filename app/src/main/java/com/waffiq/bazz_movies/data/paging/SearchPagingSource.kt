package com.waffiq.bazz_movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffiq.bazz_movies.data.local.model.Search
import com.waffiq.bazz_movies.data.remote.retrofit.ApiService
import com.waffiq.bazz_movies.utils.Constants.INITIAL_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(private val apiService: ApiService, private val query: String) : PagingSource<Int, Search>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
    return try {
      val position = params.key ?: INITIAL_PAGE_INDEX
      val responseData = apiService.search(query, position).searches

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

  override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }

}