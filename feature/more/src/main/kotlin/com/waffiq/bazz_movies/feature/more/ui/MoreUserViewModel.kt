package com.waffiq.bazz_movies.feature.more.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreUserViewModel @Inject constructor(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow<UIState>(UIState.Idle)
  val state: StateFlow<UIState> get() = _state

  fun deleteSession(sessionId: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.deleteSession(sessionId).collect {
        when (it) {
          is Outcome.Loading -> _state.value = UIState.Loading
          is Outcome.Error -> _state.value = UIState.Error(it.message)
          is Outcome.Success<*> -> _state.value = UIState.Success
        }
      }
    }
  }

  fun removeState() {
    _state.value = UIState.Idle
  }
}
