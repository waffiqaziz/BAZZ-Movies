package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.local.model.SessionID
import com.waffiq.bazz_movies.data.repository.UserRepository
import kotlinx.coroutines.launch

class MoreViewModelUser(private val userRepository: UserRepository) : ViewModel() {

  fun getUserRegion() = userRepository.getUserRegion().asLiveData()
  fun saveUserRegion(region: String){
    viewModelScope.launch {
      userRepository.saveRegion(region)
    }
  }

  fun getResponse() = userRepository.postResponse

  fun getSnackBarText() = userRepository.snackBarText

  fun deleteSession(data: SessionID) = userRepository.deleteSession(data)

  fun getCountryCode() = userRepository.getCountryCode()
  fun countryCode() = userRepository.countryCode
}