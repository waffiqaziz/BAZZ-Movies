package com.waffiq.bazz_movies.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val multiSearchUseCase: MultiSearchUseCase) :
  ViewModel() {
  private val _searchResults = MutableStateFlow<PagingData<MultiSearchItem>>(
    PagingData.Companion.empty(),
  )
  val searchResults: Flow<PagingData<MultiSearchItem>> = _searchResults.cachedIn(
    viewModelScope,
  )

  fun search(query: String) {
    viewModelScope.launch {
      multiSearchUseCase.search(query).collectLatest {
        _searchResults.value = it
      }
    }
  }
}
