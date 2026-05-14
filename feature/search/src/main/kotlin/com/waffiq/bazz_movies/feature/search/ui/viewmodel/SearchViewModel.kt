package com.waffiq.bazz_movies.feature.search.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val multiSearchUseCase: MultiSearchUseCase,
  private val searchHistoryUseCase: SearchHistoryLocalDatabaseUseCase,
) : ViewModel() {

  private val _searchResults =
    MutableStateFlow<PagingData<MultiSearchItem>>(PagingData.Companion.empty())
  val searchResults: Flow<PagingData<MultiSearchItem>> =
    _searchResults.cachedIn(viewModelScope)

  val searchHistory: StateFlow<List<SearchHistory>> =
    searchHistoryUseCase.getSearchHistory()
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT), emptyList())

  fun search(query: String) {
    viewModelScope.launch {
      searchHistoryUseCase.insert(SearchHistory(1, query, 1L))
      searchHistoryUseCase.trimHistory()

      multiSearchUseCase.search(query).collectLatest {
        _searchResults.value = it
      }
    }
  }

  fun deleteHistory(item: SearchHistory) {
    viewModelScope.launch {
      searchHistoryUseCase.delete(item)
    }
  }

  fun deleteAllHistory() {
    viewModelScope.launch {
      searchHistoryUseCase.deleteAll()
    }
  }

  companion object {
    private const val TIMEOUT = 5000L
  }
}
