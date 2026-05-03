package com.waffiq.bazz_movies.feature.home.testutils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.delay

class TestPagingSource(
  private val items: List<MediaItem>,
  private val shouldError: Boolean = false,
) : PagingSource<Int, MediaItem>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
    delay(100)
    return if (shouldError) {
      LoadResult.Error(RuntimeException("Fake network error"))
    } else {
      LoadResult.Page(
        data = items,
        prevKey = null,
        nextKey = null, // single page, no pagination
      )
    }
  }

  override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? = null
}
