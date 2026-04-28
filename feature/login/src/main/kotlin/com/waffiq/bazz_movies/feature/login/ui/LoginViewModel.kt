package com.waffiq.bazz_movies.feature.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase,
  private val userPrefUseCase: UserPrefUseCase,
) : ViewModel() {

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableLiveData<String>()
  val errorState: LiveData<String> get() = _errorState

  private val _loginState = MutableLiveData<Boolean>()
  val loginState: LiveData<Boolean> get() = _loginState

  fun userLogin(username: String, password: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.login(username, password).collect { outcome ->
        when (outcome) {
          is Outcome.Loading -> _loadingState.value = true

          is Outcome.Error -> {
            _loadingState.value = false
            _loginState.value = false
            _errorState.value = outcome.message
          }

          is Outcome.Success -> {
            _loadingState.value = false
            _loginState.value = true
          }
        }
      }
    }
  }

  fun saveGuestUserPref(name: String, username: String) {
    val guestModel = UserModel(
      userId = 0,
      name = name,
      username = username,
      password = NAN,
      region = NAN,
      token = NAN,
      isLogin = true,
      gravatarHash = null,
      tmdbAvatar = null,
    )
    viewModelScope.launch {
      userPrefUseCase.saveUserPref(guestModel)
    }
  }
}
