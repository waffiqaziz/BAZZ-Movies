package com.waffiq.bazz_movies.feature.search.ui

sealed interface SearchScreenState {

  // No search has been performed yet
  data object Browse : SearchScreenState

  // Loadin in progress and no items are available
  data object Loading : SearchScreenState

  // item is fetched (refresh already completed) and there are still zero items
  data object FetchingMore : SearchScreenState

  // show the content (search result)
  data object Content : SearchScreenState

  // search is completed successfully but no result (zero items)
  data object NoResults : SearchScreenState

  // error occurs
  data class Error(val cause: Throwable) : SearchScreenState
}
