package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailUserPrefViewModel @Inject constructor(
  private val userPrefUseCase: UserPrefUseCase
) : ViewModel() {
  fun getUserToken() = userPrefUseCase.getUserToken().asLiveData().distinctUntilChanged()

  fun getUserRegion() = userPrefUseCase.getUserRegionPref().asLiveData().distinctUntilChanged()

  fun getUserPref() = userPrefUseCase.getUser().asLiveData().distinctUntilChanged()
}
