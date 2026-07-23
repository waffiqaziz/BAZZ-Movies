package com.waffiq.bazz_movies.feature.home.testutils.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffiq.bazz_movies.core.models.MediaItem
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

class NeverLoadingPagingSource : PagingSource<Int, MediaItem>() {
  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
    delay(Long.MAX_VALUE) // suspends forever, so adapter stays in Loading state
    throw CancellationException()
  }

  override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? = null
}
