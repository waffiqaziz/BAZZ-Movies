package com.waffiq.bazz_movies.feature.detail.ui.state

import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider

sealed class WatchProvidersUiState {
  object Loading : WatchProvidersUiState()
  data class Success(
    val flatrate: List<Provider>,
    val rent: List<Provider>,
    val buy: List<Provider>,
    val free: List<Provider>
  ) : WatchProvidersUiState()
  data class Error(val message: String) : WatchProvidersUiState()
}
