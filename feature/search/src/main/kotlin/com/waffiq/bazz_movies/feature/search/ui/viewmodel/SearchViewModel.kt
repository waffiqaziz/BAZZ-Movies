package com.waffiq.bazz_movies.feature.search.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.models.SearchHistory
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val multiSearchUseCase: MultiSearchUseCase,
  private val searchHistoryUseCase: SearchHistoryLocalDatabaseUseCase,
) : ViewModel() {

  private val _searchResults = MutableStateFlow(PagingData.empty<MultiSearchItem>())
  val searchResults: Flow<PagingData<MultiSearchItem>> = _searchResults.cachedIn(viewModelScope)

  private val _currentQuery = MutableStateFlow<String?>(null)
  val currentQuery: StateFlow<String?> = _currentQuery.asStateFlow()

  val searchHistory: StateFlow<List<SearchHistory>> =
    searchHistoryUseCase.getSearchHistory().debounce(DEBOUNCE_SHORT.milliseconds)
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(HISTORY_TIMEOUT), emptyList())

  fun search(query: String) {
    if (query == _currentQuery.value) return
    _currentQuery.value = query

    viewModelScope.launch {
      searchHistoryUseCase.insert(
        SearchHistory(id = 0, query = query, createdAt = System.currentTimeMillis()),
      )
      searchHistoryUseCase.trimHistory()
    }

    viewModelScope.launch {
      multiSearchUseCase.search(query).collectLatest { pagingData ->
        _searchResults.value = pagingData
      }
    }
  }

  fun deleteHistory(item: SearchHistory) {
    viewModelScope.launch { searchHistoryUseCase.delete(item) }
  }

  fun deleteAllHistory() {
    viewModelScope.launch { searchHistoryUseCase.deleteAll() }
  }

  fun clearSearch() {
    _currentQuery.value = null
    _searchResults.value = PagingData.empty()
  }

  private companion object {
    const val HISTORY_TIMEOUT = 5_000L
  }
}
