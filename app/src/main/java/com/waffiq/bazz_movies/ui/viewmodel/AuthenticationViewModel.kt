package com.waffiq.bazz_movies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.utils.resultstate.Status
import com.waffiq.bazz_movies.utils.common.Event
import kotlinx.coroutines.launch

class AuthenticationViewModel(
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase,
) : ViewModel() {

  private val _userModel = MutableLiveData<UserModel>()
  val userModel: LiveData<UserModel> get() = _userModel

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  private val _loginState = MutableLiveData<Boolean>()
  val loginState: LiveData<Boolean> get() = _loginState

  // 1. Create a new request token
  fun userLogin(username: String, password: String) {
    _loginState.value = false
    viewModelScope.launch {
      authTMDbAccountUseCase.createToken().collect { resultCreateToken ->
        when (resultCreateToken.status) {
          Status.SUCCESS -> {
            if (resultCreateToken.data?.success == true && resultCreateToken.data.requestToken != null) {
              login(username, password, resultCreateToken.data.requestToken)
            } else {
              _loginState.value = false
            }
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value =
              Event(resultCreateToken.message ?: "Something went wrong. Please try again later.")
          }
        }
      }
    }
  }

  // 2. authorize the request token
  private fun login(username: String, password: String, requestToken: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.login(username, password, requestToken).collect { resultLogin ->
        when (resultLogin.status) {
          Status.SUCCESS -> {
            resultLogin.data?.requestToken.let { token ->
              if (token != null) createSession(token) else _loginState.value = false
            }
          }

          Status.LOADING -> _loadingState.postValue(true)
          Status.ERROR -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value =
              Event(
                resultLogin.message ?: "Something went wrong. Please try again later."
              )
          }
        }
      }
    }
  }

  // 3. Create a new session id with the authorized request token
  private fun createSession(token: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.createSessionLogin(token).collect { result ->
        when (result.status) {
          Status.SUCCESS -> {
            result.data.let {
              if (it?.success == true) {
                getUserDetail(it.sessionId)
              } else {
                _loginState.value = false
              }
            }
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value =
              Event(
                result.message
                  ?: "Something went wrong. Please try again later."
              )
          }
        }
      }
    }
  }

  private fun getUserDetail(sessionId: String) {
    viewModelScope.launch {
      authTMDbAccountUseCase.getUserDetail(sessionId).collect { result ->
        when (result.status) {
          Status.SUCCESS -> {
            if (result.data != null) {
              _userModel.value = UserModel(
                userId = result.data.id ?: 0,
                name = result.data.name.toString(),
                username = result.data.username.toString(),
                password = "NaN",
                region = "NaN",
                token = sessionId,
                isLogin = true,
                gravatarHast = result.data.avatarItem?.gravatar?.hash,
                tmdbAvatar = result.data.avatarItem?.avatarTMDb?.avatarPath
              )
              _loginState.value = true
            } else {
              _loginState.value = false
            }
            _loadingState.value = false
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loginState.value = false
            _loadingState.value = false
            _errorState.value =
              Event(result.message ?: "Something went wrong. Please try again later.")
          }
        }
      }
    }
  }
}
