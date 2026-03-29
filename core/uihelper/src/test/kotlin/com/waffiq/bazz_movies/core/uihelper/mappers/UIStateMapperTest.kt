package com.waffiq.bazz_movies.core.uihelper.mappers

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.waffiq.bazz_movies.core.uihelper.mappers.UIStateMapper.toUiState
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import org.junit.Assert.assertEquals
import org.junit.Test

class UIStateMapperTest {

  @Test
  fun toUiState_whenRefreshIsLoading_returnsUiStateLoading() {
    val loadStates = CombinedLoadStates(
      refresh = LoadState.Loading,
      prepend = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = false),
      source = LoadStates(
        refresh = LoadState.Loading,
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
      )
    )

    val result = loadStates.toUiState()
    assertEquals(UIState.Loading, result)
  }

  @Test
  fun toUiState_whenRefreshIsNotLoading_returnsUiStateSuccess() {
    val loadStates = CombinedLoadStates(
      refresh = LoadState.NotLoading(endOfPaginationReached = false),
      prepend = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = false),
      source = LoadStates(
        refresh = LoadState.NotLoading(endOfPaginationReached = false),
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
      )
    )

    val result = loadStates.toUiState()
    assertEquals(UIState.Success(Unit), result)
  }

  @Test
  fun toUiState_whenRefreshIsErrorWithMessage_returnsUiStateErrorWithMessage() {
    val errorMessage = "Something went wrong"
    val loadStates = CombinedLoadStates(
      refresh = LoadState.Error(Throwable(errorMessage)),
      prepend = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = false),
      source = LoadStates(
        refresh = LoadState.Error(Throwable(errorMessage)),
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
      )
    )

    val result = loadStates.toUiState()
    assertEquals(UIState.Error(errorMessage), result)
  }

  @Test
  fun toUiState_whenRefreshIsErrorWithNullMessage_returnsUiStateErrorWithFallbackMessage() {
    val loadStates = CombinedLoadStates(
      refresh = LoadState.Error(Throwable()),
      prepend = LoadState.NotLoading(endOfPaginationReached = false),
      append = LoadState.NotLoading(endOfPaginationReached = false),
      source = LoadStates(
        refresh = LoadState.Error(Throwable()),
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
      )
    )

    val result = loadStates.toUiState()
    assertEquals(UIState.Error("Unknown error"), result)
  }
}
