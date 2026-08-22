package com.waffiq.bazz_movies.feature.detail.utils.helpers

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter

object PagingDataHelper {

  fun PagingDataAdapter<*, *>.observeEmptyState(onEmpty: (Boolean) -> Unit) {
    addLoadStateListener { loadStates ->
      val isLoading = loadStates.refresh is LoadState.Loading
      val isEmpty = !isLoading && itemCount == 0

      onEmpty(isEmpty)
    }
  }
}
