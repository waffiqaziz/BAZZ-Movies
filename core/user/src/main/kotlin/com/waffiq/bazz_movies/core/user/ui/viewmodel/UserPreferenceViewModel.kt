package com.waffiq.bazz_movies.core.user.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferenceViewModel @Inject constructor(private val userPrefUseCase: UserPrefUseCase) :
  ViewModel() {

  fun getUserPref(): LiveData<UserModel> = userPrefUseCase.getUser().asLiveData()

  fun getUserRegionPref(): LiveData<String> = userPrefUseCase.getUserRegionPref().asLiveData()

  fun saveUserPref(userModel: UserModel) {
    viewModelScope.launch { userPrefUseCase.saveUserPref(userModel) }
  }

  fun saveRegionPref(region: String) {
    viewModelScope.launch { userPrefUseCase.saveRegionPref(region) }
  }

  fun removeUserDataPref() {
    viewModelScope.launch { userPrefUseCase.removeUserDataPref() }
  }

  fun savePermissionAsked() {
    viewModelScope.launch { userPrefUseCase.savePermissionAsked() }
  }

  fun getPermissionAsked(): LiveData<Boolean> =
    userPrefUseCase.getPermissionAsked().asLiveData()
}
