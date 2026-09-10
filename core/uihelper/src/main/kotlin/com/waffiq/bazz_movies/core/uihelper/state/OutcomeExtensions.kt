package com.waffiq.bazz_movies.core.uihelper.state

import com.waffiq.bazz_movies.core.model.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T> Flow<Outcome<T>>.asUiState(): Flow<UIState<T>> =
  map { outcome ->
    when (outcome) {
      is Outcome.Success -> UIState.Success(outcome.data)
      is Outcome.Loading -> UIState.Loading
      is Outcome.Error -> UIState.Error(outcome.message)
    }
  }
