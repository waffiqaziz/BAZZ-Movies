package com.waffiq.bazz_movies.core.uihelper.mappers

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.waffiq.bazz_movies.core.uihelper.state.UIState

object UIStateMapper {
  fun CombinedLoadStates.toUiState(): UIState<Unit> =
    when (val refresh = this.refresh) {
      is LoadState.Loading -> UIState.Loading
      is LoadState.NotLoading -> UIState.Success(Unit)
      is LoadState.Error -> UIState.Error(refresh.error.localizedMessage ?: "Unknown error")
    }
}
