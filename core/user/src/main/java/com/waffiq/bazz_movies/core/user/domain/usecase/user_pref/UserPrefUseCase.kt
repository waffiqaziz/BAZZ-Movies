package com.waffiq.bazz_movies.core.user.domain.usecase.user_pref

import com.waffiq.bazz_movies.core.user.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserPrefUseCase {
  fun getUser(): Flow<UserModel>
  fun getUserRegionPref(): Flow<String>
  fun getUserToken(): Flow<String>
  suspend fun saveRegionPref(region: String)
  suspend fun saveUserPref(userModel: UserModel)
  suspend fun removeUserDataPref()
}
