package com.waffiq.bazz_movies.feature.more.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreUserViewModel @Inject constructor(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase
) : ViewModel() {

  private val _signOutState = MutableStateFlow<Outcome<Post>?>(null)
  val signOutState: Flow<Outcome<Post>?> get() = _signOutState

  fun deleteSession(sessionId: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.deleteSession(sessionId).collect { outcome ->
        _signOutState.value = outcome
      }
    }
  }

  fun removeState() {
    _signOutState.value = Outcome.Loading
  }
}
