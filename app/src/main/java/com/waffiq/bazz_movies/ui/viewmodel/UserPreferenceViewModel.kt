package com.waffiq.bazz_movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefUseCase
import kotlinx.coroutines.launch

class UserPreferenceViewModel(
  private val userPrefUseCase: UserPrefUseCase
) : ViewModel() {

  fun getUserPref() = userPrefUseCase.getUser().asLiveData()

  fun getUserRegionPref() = userPrefUseCase.getUserRegionPref().asLiveData()

  fun saveUserPref(userModel: UserModel) {
    viewModelScope.launch { userPrefUseCase.saveUserPref(userModel) }
  }

  fun saveRegionPref(region: String) {
    viewModelScope.launch { userPrefUseCase.saveRegionPref(region) }
  }

  fun removeUserDataPref() {
    viewModelScope.launch { userPrefUseCase.removeUserDataPref() }
  }
}