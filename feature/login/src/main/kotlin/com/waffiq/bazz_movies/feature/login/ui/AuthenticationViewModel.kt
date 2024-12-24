package com.waffiq.bazz_movies.feature.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.data.model.UserModel
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase,
) : ViewModel() {

  private val _userModel = MutableLiveData<UserModel>()
  val userModel: LiveData<UserModel> get() = _userModel

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableLiveData<String>()
  val errorState: LiveData<String> get() = _errorState

  private val _loginState = MutableLiveData<Boolean>()
  val loginState: LiveData<Boolean> get() = _loginState

  // 1. Create a new request token
  fun userLogin(username: String, password: String) {
    _loginState.value = false
    viewModelScope.launch {
      authTMDbAccountUseCase.createToken().collect { resultCreateToken ->
        when (resultCreateToken) {
          is NetworkResult.Success -> {
            if (resultCreateToken.data.success) {
              resultCreateToken.data.requestToken?.let { login(username, password, it) }
            } else {
              _loginState.value = false
            }
          }

          is NetworkResult.Loading -> _loadingState.value = true
          is NetworkResult.Error -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value = resultCreateToken.message
          }
        }
      }
    }
  }

  // 2. authorize the request token
  private fun login(username: String, password: String, requestToken: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.login(username, password, requestToken).collect { resultLogin ->
        when (resultLogin) {
          is NetworkResult.Success -> {
            resultLogin.data.requestToken.let {
              if (it != null) createSession(it) else _loginState.value = false
            }
          }

          is NetworkResult.Loading -> _loadingState.postValue(true)
          is NetworkResult.Error -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value = resultLogin.message
          }
        }
      }
    }
  }

  // 3. Create a new session id with the authorized request token
  private fun createSession(requestToken: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.createSessionLogin(requestToken).collect { result ->
        when (result) {
          is NetworkResult.Success -> {
            result.data.let {
              if (it.success) getUserDetail(it.sessionId) else _loginState.value = false
            }
          }

          is NetworkResult.Loading -> _loadingState.value = true
          is NetworkResult.Error -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value = result.message
          }
        }
      }
    }
  }

  private fun getUserDetail(sessionId: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.getUserDetail(sessionId).collect { result ->
        when (result) {
          is NetworkResult.Success -> {
            _userModel.value = UserModel(
              userId = result.data.id ?: 0,
              name = result.data.name.toString(),
              username = result.data.username.toString(),
              password = NAN,
              region = NAN,
              token = sessionId,
              isLogin = true,
              gravatarHast = result.data.avatarItem?.gravatar?.hash,
              tmdbAvatar = result.data.avatarItem?.avatarTMDb?.avatarPath
            )
            _loginState.value = true
            _loadingState.value = false
          }

          is NetworkResult.Loading -> _loadingState.value = true
          is NetworkResult.Error -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value = result.message
          }
        }
      }
    }
  }
}
