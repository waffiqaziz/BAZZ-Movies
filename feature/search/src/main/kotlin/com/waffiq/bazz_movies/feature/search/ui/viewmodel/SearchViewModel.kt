package com.waffiq.bazz_movies.feature.search.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.common.MediaType
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val multiSearchUseCase: MultiSearchUseCase,
  private val searchHistoryUseCase: SearchHistoryLocalDatabaseUseCase,
) : ViewModel() {

  private val _currentQuery = MutableStateFlow<String?>(null)
  val currentQuery: StateFlow<String?> = _currentQuery.asStateFlow()

  private val _selectedFilters = MutableStateFlow(setOf(MediaType.MULTI))
  val selectedFilters: StateFlow<Set<MediaType>> = _selectedFilters.asStateFlow()

  // any change to query OR filters cancels pager and starts a fresh one
  val searchResults: Flow<PagingData<MultiSearchItem>> =
    combine(_currentQuery, _selectedFilters) { query, filters -> query?.let { it to filters } }
      .distinctUntilChanged()
      .flatMapLatest { pair ->
        if (pair == null) {
          flowOf(PagingData.empty())
        } else {
          val (query, filters) = pair
          multiSearchUseCase.search(query, filters)
        }
      }
      .cachedIn(viewModelScope)

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
  }

  fun setFilters(filters: Set<MediaType>) {
    _selectedFilters.value = filters.ifEmpty { setOf(MediaType.MULTI) }
  }

  fun deleteHistory(item: SearchHistory) {
    viewModelScope.launch { searchHistoryUseCase.delete(item) }
  }

  fun deleteAllHistory() {
    viewModelScope.launch { searchHistoryUseCase.deleteAll() }
  }

  fun clearSearch() {
    _currentQuery.value = null
    _selectedFilters.value = setOf(MediaType.MULTI)
  }

  private companion object {
    const val HISTORY_TIMEOUT = 5_000L
  }
}
