package com.waffiq.bazz_movies.ui.activity.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.domain.model.search.ResultsItemSearch
import com.waffiq.bazz_movies.domain.usecase.multi_search.MultiSearchUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(
  private val multiSearchUseCase: MultiSearchUseCase
) : ViewModel() {
  private val _searchResults = MutableStateFlow<PagingData<ResultsItemSearch>>(PagingData.empty())
  val searchResults: Flow<PagingData<ResultsItemSearch>> = _searchResults.cachedIn(viewModelScope)

  fun search(query: String) {
    viewModelScope.launch {
      multiSearchUseCase.search(query).collectLatest {
        _searchResults.value = it
      }
    }
  }
}