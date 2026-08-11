package com.waffiq.bazz_movies.feature.search.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.waffiq.bazz_movies.feature.search.ui.SearchScreenState

/**
 * Maps Paging 3 [CombinedLoadStates] with adapter current item count into a
 * [SearchScreenState].
 */
object SearchLoadStateMapper {

  fun map(
    loadState: CombinedLoadStates,
    itemCount: Int,
    hasActiveQuery: Boolean,
  ): SearchScreenState {
    if (!hasActiveQuery) return SearchScreenState.Browse

    return when (val refresh = loadState.source.refresh) {
      is LoadState.Loading -> SearchScreenState.Loading
      is LoadState.Error -> SearchScreenState.Error(refresh.error)
      is LoadState.NotLoading -> mapNotLoading(loadState, itemCount)
    }
  }

  private fun mapNotLoading(loadState: CombinedLoadStates, itemCount: Int): SearchScreenState {
    val pendingError = pagingError(loadState)
    return when {
      pendingError != null && itemCount < 1 -> SearchScreenState.Error(pendingError.error)
      loadState.append.endOfPaginationReached && itemCount < 1 -> SearchScreenState.NoResults
      itemCount < 1 -> SearchScreenState.FetchingMore
      else -> SearchScreenState.Content
    }
  }

  /**
   * First error found across append, prepend or refresh, in  priority order.
   */
  fun pagingError(loadState: CombinedLoadStates): LoadState.Error? =
    when {
      loadState.append is LoadState.Error -> loadState.append as LoadState.Error
      loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
      loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
      else -> null
    }
}
