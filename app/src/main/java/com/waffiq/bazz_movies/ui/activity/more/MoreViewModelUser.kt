package com.waffiq.bazz_movies.ui.activity.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.repository.UserRepository
import kotlinx.coroutines.launch

class MoreViewModelUser(private val userRepository: UserRepository) : ViewModel() {

  fun getUserRegion() = userRepository.getUserRegion().asLiveData()
  fun saveUserRegion(region: String){
    viewModelScope.launch {
      userRepository.saveRegion(region)
    }
  }

  fun getSnackBarText() = userRepository.snackBarText

  fun getCountryCode() = userRepository.getCountryCode()
  fun countryCode() = userRepository.countryCode
}