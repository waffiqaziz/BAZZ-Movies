package com.waffiq.bazz_movies.feature.detail.utils.helpers

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

object PagingDataHelper {

  fun PagingDataAdapter<*, *>.observeEmptyState(
    scope: CoroutineScope,
    onEmpty: (Boolean) -> Unit,
  ) {
    scope.launch {
      emptyStateFlow(
        loadStateFlow = loadStateFlow,
        onPagesUpdatedFlow = onPagesUpdatedFlow,
        itemCountProvider = { itemCount },
      ).collectLatest(onEmpty)
    }
  }

  internal fun emptyStateFlow(
    loadStateFlow: Flow<CombinedLoadStates>,
    onPagesUpdatedFlow: Flow<Unit>,
    itemCountProvider: () -> Int,
  ): Flow<Boolean> =
    loadStateFlow
      .combine(onPagesUpdatedFlow.onStart { emit(Unit) }) { loadStates, _ -> loadStates }
      .map { loadStates -> loadStates.refresh !is LoadState.Loading && itemCountProvider() == 0 }
}
