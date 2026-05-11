package com.waffiq.bazz_movies.feature.home.testutils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffiq.bazz_movies.core.models.MediaItem

class SuccessThenErrorPagingSource(private val items: List<MediaItem>) :
  PagingSource<Int, MediaItem>() {
  private var loadCount = 0

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
    loadCount++
    return if (loadCount == 1) {
      // First load → success with items
      LoadResult.Page(data = items, prevKey = null, nextKey = null)
    } else {
      // Subsequent loads (refresh) → error
      LoadResult.Error(RuntimeException("Fake network error"))
    }
  }

  override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? = null
}
