package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailCollections
import com.waffiq.bazz_movies.feature.detail.domain.usecase.collection.GetMovieCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
  private val getMovieCollectionUseCase: GetMovieCollectionUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow<UIState<DetailCollections>>(UIState.Idle)
  val uiState = _uiState.asStateFlow()

  fun loadMovieCollection(collectionId: Int) {
    viewModelScope.launch {
      getMovieCollectionUseCase.getMovieCollection(collectionId)
        .collect { outcome ->
          _uiState.value = when (outcome) {
            is Outcome.Success -> UIState.Success(outcome.data)
            is Outcome.Error -> UIState.Error(outcome.message)
            is Outcome.Loading -> UIState.Loading
          }
        }
    }
  }
}
