package com.waffiq.bazz_movies.core.user.testutils

import com.waffiq.bazz_movies.core.user.data.model.UserModel
import com.waffiq.bazz_movies.core.user.domain.usecase.user_pref.UserPrefUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeUserPrefUseCase @Inject constructor() : UserPrefUseCase {

  private val userFlow = MutableStateFlow(
    UserModel(
      userId = 12345,
      name = "User Testing",
      username = "user_testing",
      password = "pass_testing",
      region = "id",
      token = "token_testing",
      isLogin = false,
      gravatarHast = null,
      tmdbAvatar = null
    )
  )
  private val regionFlow = MutableStateFlow("CN")

  override fun getUser(): Flow<UserModel> = userFlow

  override fun getUserRegionPref(): Flow<String> = regionFlow

  override fun getUserToken(): Flow<String> = flowOf("test_token")

  override suspend fun saveRegionPref(region: String) {
    regionFlow.value = region
  }

  override suspend fun saveUserPref(userModel: UserModel) {
    userFlow.value = userModel
  }

  override suspend fun removeUserDataPref() {
    userFlow.value = UserModel(
      userId = 0,
      name = "",
      username = "",
      password = "",
      region = "",
      token = "",
      isLogin = false,
      gravatarHast = "",
      tmdbAvatar = "",
    )
    regionFlow.value = ""
  }

  // Helper functions to simulate test scenarios
  fun setUser(userModel: UserModel) {
    userFlow.value = userModel
  }

  fun setRegion(region: String) {
    regionFlow.value = region
  }
}
