package com.waffiq.bazz_movies.core.data.repository

import com.waffiq.bazz_movies.core.data.local.model.UserModel
import com.waffiq.bazz_movies.core.data.local.model.UserPreference
import com.waffiq.bazz_movies.core.data.remote.datasource.UserDataSource
import com.waffiq.bazz_movies.core.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.core.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.domain.repository.IUserRepository
import com.waffiq.bazz_movies.core.utils.mappers.AccountMapper.toAccountDetails
import com.waffiq.bazz_movies.core.utils.mappers.AccountMapper.toAuthentication
import com.waffiq.bazz_movies.core.utils.mappers.AccountMapper.toCountryIP
import com.waffiq.bazz_movies.core.utils.mappers.AccountMapper.toCreateSession
import com.waffiq.bazz_movies.core.utils.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
  private val pref: UserPreference,
  private val userDataSource: UserDataSource
) : IUserRepository {

  // region AUTH
  override suspend fun createToken(): Flow<NetworkResult<Authentication>> =
    userDataSource.createToken().map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toAuthentication())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun login(
    username: String,
    pass: String,
    token: String
  ): Flow<NetworkResult<Authentication>> =
    userDataSource.login(username, pass, token).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toAuthentication())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSession>> =
    userDataSource.createSessionLogin(token).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toCreateSession())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<Post>> =
    userDataSource.deleteSession(data).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toPost())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>> =
    userDataSource.getUserDetail(sessionId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toAccountDetails())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }
  // endregion AUTH

  // region PREF
  override suspend fun saveUserPref(userModel: UserModel) = pref.saveUser(userModel)

  override suspend fun saveRegionPref(region: String) = pref.saveRegion(region)

  override fun getUserPref(): Flow<UserModel> = pref.getUser()

  override fun getUserToken(): Flow<String> = pref.getToken()

  override fun getUserRegionPref(): Flow<String> = pref.getRegion()

  override suspend fun removeUserDataPref() = pref.removeUserData()
  // endregion PREF

  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>> =
    userDataSource.getCountryCode().map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toCountryIP())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }
}
