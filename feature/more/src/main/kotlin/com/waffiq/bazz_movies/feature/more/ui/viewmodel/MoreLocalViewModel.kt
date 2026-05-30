package com.waffiq.bazz_movies.feature.more.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.database.domain.usecase.DatabaseBackupUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreLocalViewModel @Inject constructor(
  private val localDatabaseUseCase: FavoriteLocalDatabaseUseCase,
  private val searchHistoryLocalDatabaseUseCase: SearchHistoryLocalDatabaseUseCase,
  private val databaseBackupUseCase: DatabaseBackupUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  val state: StateFlow<UIState<Unit>> get() = _state

  private val _backupState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  val backupState: StateFlow<UIState<Unit>> get() = _backupState

  private val _restoreState = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  val restoreState: StateFlow<UIState<Unit>> get() = _restoreState

  fun deleteAll() {
    viewModelScope.launch {
      _state.value = UIState.Loading
      delay(timeMillis = 450)

      when (val result = localDatabaseUseCase.deleteAll()) {
        is DbResult.Success -> _state.value = UIState.Success(Unit)
        is DbResult.Error -> _state.value = UIState.Error(result.errorMessage)
      }
    }
  }

  fun deleteAllSearchHistory() {
    viewModelScope.launch {
      searchHistoryLocalDatabaseUseCase.deleteAll()
    }
  }

  fun backupDatabase(destinationUri: Uri) {
    viewModelScope.launch {
      _backupState.value = UIState.Loading
      _backupState.value =
        when (val result = databaseBackupUseCase.backupDatabase(destinationUri)) {
          is DbResult.Success -> UIState.Success(Unit)
          is DbResult.Error -> UIState.Error(result.errorMessage)
        }
    }
  }

  fun restoreDatabase(sourceUri: Uri) {
    viewModelScope.launch {
      _restoreState.value = UIState.Loading
      _restoreState.value = when (val result = databaseBackupUseCase.restoreDatabase(sourceUri)) {
        is DbResult.Success -> UIState.Success(Unit)
        is DbResult.Error -> UIState.Error(result.errorMessage)
      }
    }
  }

  fun resetBackupState() {
    _backupState.value = UIState.Idle
  }

  fun resetRestoreState() {
    _restoreState.value = UIState.Idle
  }
}
