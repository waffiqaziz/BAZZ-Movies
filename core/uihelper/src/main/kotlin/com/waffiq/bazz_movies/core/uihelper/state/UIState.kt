package com.waffiq.bazz_movies.core.uihelper.state

sealed interface UIState {
  data object Idle : UIState
  data object Loading : UIState
  data object Success : UIState
  data class Error(val message: String) : UIState
}

val UIState.isLoading: Boolean
  get() = this is UIState.Loading
