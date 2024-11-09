package com.waffiq.bazz_movies.core.user.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.user.data.model.UserModel
import com.waffiq.bazz_movies.core.user.domain.usecase.user_pref.UserPrefUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferenceViewModel @Inject constructor(
  private val userPrefUseCase: UserPrefUseCase
) : ViewModel() {

  fun getUserPref() = userPrefUseCase.getUser().asLiveData().distinctUntilChanged()

  fun getUserRegionPref() = userPrefUseCase.getUserRegionPref().asLiveData().distinctUntilChanged()

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
