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
    val isEmpty = itemCount < 1
    val pendingError = pagingError(loadState)
    val paginationExhausted = loadState.append.endOfPaginationReached

    return when {
      !isEmpty -> SearchScreenState.Content
      pendingError != null -> SearchScreenState.Error(pendingError.error)
      paginationExhausted -> SearchScreenState.NoResults
      else -> SearchScreenState.FetchingMore
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
