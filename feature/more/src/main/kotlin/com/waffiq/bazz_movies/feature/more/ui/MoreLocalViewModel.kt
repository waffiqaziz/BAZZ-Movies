package com.waffiq.bazz_movies.feature.more.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
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
  private val localDatabaseUseCase: LocalDatabaseUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow<UIState<Unit>>(UIState.Idle)
  val state: StateFlow<UIState<Unit>> get() = _state

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
}
