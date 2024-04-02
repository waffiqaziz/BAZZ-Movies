package com.waffiq.bazz_movies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val userRepository: UserRepository) : ViewModel() {

  private var _token = MutableLiveData<String>()
  val token: LiveData<String> get() = _token

  private var _sessionId = MutableLiveData<String>()
  val sessionId: LiveData<String> get() = _sessionId

  private val _userModel = MutableLiveData<UserModel>()
  val userModel: LiveData<UserModel> get() = _userModel

  private var _tokenVerified = MutableLiveData<String>()
  val tokenVerified: LiveData<String> get() = _tokenVerified

  private val _loadingState = MutableLiveData<Boolean>()
  val loadingState: LiveData<Boolean> get() = _loadingState

  private val _errorState = MutableLiveData<Event<String>>()
  val errorState: LiveData<Event<String>> get() = _errorState

  fun getUserPref() = userRepository.getUser().asLiveData()

  fun createToken() {
    viewModelScope.launch {
      userRepository.createToken().collect { result ->
        when (result.status) {
          Status.SUCCESS -> {
            result.data.let {
              if (it?.success == true) {
                _token.value = it.requestToken
              }
            }
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value =
              Event(result.message ?: "Something went wrong. Please try again later.")
          }
        }
      }
    }
  }

  fun login(username: String, password: String, token: String) {
    viewModelScope.launch(Dispatchers.IO) {
      val result = userRepository.login(username, password, token)
      when (result.status) {
        Status.SUCCESS -> {
          if (result.data?.success == true)
            _tokenVerified.postValue(result.data.requestToken)
        }

        Status.LOADING -> _loadingState.postValue(true)
        Status.ERROR -> {
          _loadingState.postValue(false)
          _errorState.postValue(
            Event(result.message ?: "Something went wrong. Please try again later.")
          )
        }
      }
    }
  }

  fun createSession(token: String) =
    viewModelScope.launch {
      userRepository.createSessionLogin(token).collect { result ->
        when (result.status) {
          Status.SUCCESS -> {
            result.data.let {
              if (it?.success == true) {
                _sessionId.value = it.sessionId
              }
            }
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value =
              Event(result.message ?: "Something went wrong. Please try again later.")
          }
        }
      }
    }

  fun getUserDetail(sessionId: String) =
    viewModelScope.launch {
      userRepository.getUserDetail(sessionId).collect { result ->
        when (result.status) {
          Status.SUCCESS -> {
            if (result.data != null) {
              _userModel.value = UserModel(
                userId = result.data.id ?: 0,
                name = result.data.name.toString(),
                username = result.data.username.toString(),
                password = "NaN",
                region = "NaN",
                token = "NaN",
                isLogin = true,
                gravatarHast = result.data.avatar?.gravatar?.hash,
                tmdbAvatar = result.data.avatar?.tmdb?.avatarPath
              )
            }
            _loadingState.value = false
          }

          Status.LOADING -> _loadingState.value = true
          Status.ERROR -> {
            _loadingState.value = false
            _errorState.value =
              Event(result.message ?: "Something went wrong. Please try again later.")
          }
        }
      }
    }

  fun saveUser(userModel: UserModel) {
    viewModelScope.launch { userRepository.saveUser(userModel) }
  }

  fun removeUserData() {
    viewModelScope.launch { userRepository.removeUserData() }
  }
}
