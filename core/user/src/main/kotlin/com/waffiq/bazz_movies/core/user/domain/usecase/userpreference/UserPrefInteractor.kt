package com.waffiq.bazz_movies.core.user.domain.usecase.userpreference

import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPrefInteractor @Inject constructor(
  private val userPrefRepository: IUserRepository
) : UserPrefUseCase {

  override fun getUser(): Flow<UserModel> =
    userPrefRepository.getUserPref()

  override fun getUserRegionPref(): Flow<String> =
    userPrefRepository.getUserRegionPref()

  override fun getUserToken(): Flow<String> =
    userPrefRepository.getUserToken()

  override suspend fun saveRegionPref(region: String) =
    userPrefRepository.saveRegionPref(region)

  override suspend fun saveUserPref(userModel: UserModel) =
    userPrefRepository.saveUserPref(userModel)

  override suspend fun removeUserDataPref() =
    userPrefRepository.removeUserDataPref()
}
