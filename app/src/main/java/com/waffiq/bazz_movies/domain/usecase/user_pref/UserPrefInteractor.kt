package com.waffiq.bazz_movies.domain.usecase.user_pref

import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UserPrefInteractor(
  private val userPrefRepository: IUserRepository
) : UserPrefUseCase {

  override fun getUser(): Flow<UserModel> =
    userPrefRepository.getUserPref()

  override fun getUserRegionPref(): Flow<String> =
    userPrefRepository.getUserRegionPref()

  override suspend fun saveRegionPref(region: String) =
    userPrefRepository.saveRegionPref(region)

  override suspend fun saveUserPref(userModel: UserModel) =
    userPrefRepository.saveUserPref(userModel)

  override suspend fun removeUserDataPref() =
    userPrefRepository.removeUserDataPref()
}
