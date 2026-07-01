package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.genreIds
import com.waffiq.bazz_movies.feature.detail.domain.usecase.collection.GetMovieCollectionUseCase
import com.waffiq.bazz_movies.feature.detail.ui.state.CollectionUiState
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ImageHelper.backdropOriginalSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
  private val getMovieCollectionUseCase: GetMovieCollectionUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(CollectionUiState())
  val uiState = _uiState.asStateFlow()

  fun loadMovieCollection(collectionId: Int) {
    viewModelScope.launch {
      getMovieCollectionUseCase.getMovieCollection(collectionId)
        .collect { outcome ->
          _uiState.update { currentState ->
            when (outcome) {
              is Outcome.Loading -> {
                currentState.copy(isLoading = true, isError = false)
              }

              is Outcome.Error -> {
                currentState.copy(isLoading = false, isError = true)
              }

              is Outcome.Success -> {
                val rawData = outcome.data
                val cleanParts = rawData.parts?.filterNotNull() ?: emptyList()

                currentState.copy(
                  isLoading = false,
                  isError = false,
                  name = rawData.name ?: "",
                  overview = rawData.overview ?: "",
                  genreIds = rawData.genreIds,
                  backdropUrl = rawData.backdropOriginalSource,
                  parts = cleanParts,
                )
              }
            }
          }
        }
    }
  }
}
