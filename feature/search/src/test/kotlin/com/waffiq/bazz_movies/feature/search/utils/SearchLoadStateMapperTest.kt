package com.waffiq.bazz_movies.feature.search.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.waffiq.bazz_movies.feature.search.ui.SearchScreenState
import com.waffiq.bazz_movies.feature.search.utils.SearchLoadStateMapper.map
import com.waffiq.bazz_movies.feature.search.utils.SearchLoadStateMapper.pagingError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchLoadStateMapperTest {

  private val notLoading = LoadState.NotLoading(endOfPaginationReached = false)
  private val endOfPagination = LoadState.NotLoading(endOfPaginationReached = true)
  private val loading = LoadState.Loading

  private val appendError = LoadState.Error(RuntimeException("append"))
  private val prependError = LoadState.Error(RuntimeException("prepend"))
  private val refreshError = LoadState.Error(RuntimeException("refresh"))

  private fun states(
    refresh: LoadState = notLoading,
    prepend: LoadState = notLoading,
    append: LoadState = notLoading,
  ) = CombinedLoadStates(
    refresh = refresh,
    prepend = prepend,
    append = append,
    source = LoadStates(refresh = refresh, prepend = prepend, append = append),
  )

  @Test
  fun map_noActiveQuery_returnsBrowseForAllState() {
    val result = map(
      loadState = states(refresh = loading),
      itemCount = 0,
      hasActiveQuery = false,
    )
    assertEquals(SearchScreenState.Browse, result)
  }

  @Test
  fun map_refreshLoadingNoItems_returnsLoadingState() {
    val result = map(
      loadState = states(refresh = loading),
      itemCount = 0,
      hasActiveQuery = true,
    )
    assertEquals(SearchScreenState.Loading, result)
  }

  @Test
  fun map_refreshRrror_returnsErrorState() {
    val error = RuntimeException("boom")
    val result = map(
      loadState = states(refresh = LoadState.Error(error)),
      itemCount = 0,
      hasActiveQuery = true,
    )
    assertTrue(result is SearchScreenState.Error)
    assertEquals(error, (result as SearchScreenState.Error).cause)
  }

  @Test
  fun map_appendStillInProgressWithZeroItems_returnsFetchingMore() {
    val result = map(
      loadState = states(refresh = notLoading, append = notLoading),
      itemCount = 0,
      hasActiveQuery = true,
    )
    assertEquals(SearchScreenState.FetchingMore, result)
  }

  @Test
  fun map_refreshLoading_returnsLoadingState() {
    val result = map(
      loadState = states(refresh = loading),
      itemCount = 5,
      hasActiveQuery = true,
    )
    assertEquals(SearchScreenState.Loading, result)
  }

  @Test
  fun map_endOfPaginationZeroItems_returnsNoResultsState() {
    val result = map(
      loadState = states(refresh = notLoading, append = endOfPagination),
      itemCount = 0,
      hasActiveQuery = true,
    )
    assertEquals(SearchScreenState.NoResults, result)
  }

  @Test
  fun map_itemsPresentNoErrors_returnsContentState() {
    val result = map(
      loadState = states(refresh = notLoading, append = endOfPagination),
      itemCount = 12,
      hasActiveQuery = true,
    )
    assertEquals(SearchScreenState.Content, result)
  }

  @Test
  fun map_appendErrorAndContentIsReady_stillReturnsErrorState() {
    val error = RuntimeException("page 2 failed")
    val result = map(
      loadState = states(refresh = notLoading, append = LoadState.Error(error)),
      itemCount = 0,
      hasActiveQuery = true,
    )
    assertTrue(result is SearchScreenState.Error)
  }

  @Test
  fun map_appendErrorButItemsAlreadyLoaded_returnsContentIgnoringError() {
    val result = map(
      loadState = states(refresh = notLoading, append = appendError),
      itemCount = 12,
      hasActiveQuery = true,
    )
    assertEquals(SearchScreenState.Content, result)
  }

  @Test
  fun pagingError_whenError_prioritizesAppendOverAllStates() {
    val result = pagingError(
      states(refresh = refreshError, prepend = prependError, append = appendError),
    )
    assertEquals(appendError, result)
  }

  @Test
  fun pagingError_prependError_returnsPrependError() {
    val result = pagingError(states(prepend = prependError))
    assertEquals(prependError, result)
  }

  @Test
  fun pagingError_refreshError_returnsRefreshError() {
    val result = pagingError(states(refresh = refreshError))
    assertEquals(refreshError, result)
  }
}
