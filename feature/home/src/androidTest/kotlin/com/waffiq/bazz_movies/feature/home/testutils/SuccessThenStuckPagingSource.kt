package com.waffiq.bazz_movies.feature.home.testutils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

class SuccessThenStuckPagingSource(private val items: List<MediaItem>) :
  PagingSource<Int, MediaItem>() {
  private var loadCount = 0

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
    loadCount++
    return if (loadCount == 1) {
      // First load → success with items
      LoadResult.Page(data = items, prevKey = null, nextKey = null)
    } else {
      // On refresh → stuck in loading forever
      delay(Long.MAX_VALUE)
      throw CancellationException()
    }
  }

  override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? = null
}
