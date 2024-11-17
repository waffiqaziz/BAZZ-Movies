package com.waffiq.bazz_movies.feature.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val multiSearchUseCase: MultiSearchUseCase
) : ViewModel() {
  private val _searchResults = MutableStateFlow<PagingData<ResultsItemSearch>>(
    PagingData.Companion.empty()
  )
  val searchResults: Flow<PagingData<ResultsItemSearch>> = _searchResults.cachedIn(
    viewModelScope
  )

  private val _query = MutableLiveData<String>()
  val query: LiveData<String> get() = _query

  private val _expandSearchView = MutableLiveData<Boolean>()
  val expandSearchView: LiveData<Boolean> get() = _expandSearchView

  init {
    setExpandSearchView(false)
    _query.value = ""
  }

  fun setExpandSearchView(isExpand: Boolean) {
    _expandSearchView.value = isExpand
  }

  fun search(query: String) {
    _query.value = query
    viewModelScope.launch {
      multiSearchUseCase.search(query).collectLatest {
        _searchResults.value = it
      }
    }
  }
}
