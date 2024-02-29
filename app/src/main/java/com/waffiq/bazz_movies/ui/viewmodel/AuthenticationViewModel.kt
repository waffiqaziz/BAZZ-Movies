package com.waffiq.bazz_movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val userRepository: UserRepository) : ViewModel() {

  fun getUser() = userRepository.getUser().asLiveData().distinctUntilChanged()

  fun getSnackBarText() = userRepository.snackBarText

  fun createToken() = userRepository.createToken()

  fun getToken() = userRepository.token

  fun login(username: String, password: String, token: String) =
    userRepository.login(username, password, token)

  fun getTokenVerified() = userRepository.tokenVerified

  fun createSession(token: String) = userRepository.createSessionLogin(token)

  fun getSessionId() = userRepository.sessionId

  fun getUserDetail(sessionId: String) = userRepository.getUserDetail(sessionId)

  fun getDataUserDetail() = userRepository.user

  fun saveUser(userModel: UserModel) {
    viewModelScope.launch { userRepository.saveUser(userModel) }
  }

  fun removeUserData() {
    viewModelScope.launch { userRepository.removeUserData() }
  }

  fun getSnackbar() = userRepository.snackBarText

  fun getLoading() = userRepository.isLoading
}
