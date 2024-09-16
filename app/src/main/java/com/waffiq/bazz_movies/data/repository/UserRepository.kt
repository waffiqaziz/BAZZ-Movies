package com.waffiq.bazz_movies.data.repository

import com.waffiq.bazz_movies.data.local.model.UserModel
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.data.remote.datasource.UserDataSource
import com.waffiq.bazz_movies.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.domain.model.account.Authentication
import com.waffiq.bazz_movies.domain.model.account.CountryIP
import com.waffiq.bazz_movies.domain.model.account.CreateSession
import com.waffiq.bazz_movies.domain.model.post.Post
import com.waffiq.bazz_movies.domain.repository.IUserRepository
import com.waffiq.bazz_movies.utils.mappers.AccountMapper.toAccountDetails
import com.waffiq.bazz_movies.utils.mappers.AccountMapper.toAuthentication
import com.waffiq.bazz_movies.utils.mappers.AccountMapper.toCountryIP
import com.waffiq.bazz_movies.utils.mappers.AccountMapper.toCreateSession
import com.waffiq.bazz_movies.utils.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import com.waffiq.bazz_movies.utils.resultstate.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
  private val pref: UserPreference,
  private val userDataSource: UserDataSource
) : IUserRepository {

  // region AUTH
  override suspend fun createToken(): Flow<NetworkResult<Authentication>> =
    userDataSource.createToken().map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toAuthentication())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun login(
    username: String,
    pass: String,
    token: String
  ): Flow<NetworkResult<Authentication>> =
    userDataSource.login(username, pass, token).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toAuthentication())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSession>> =
    userDataSource.createSessionLogin(token).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toCreateSession())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<Post>> =
    userDataSource.deleteSession(data).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toPost())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>> =
    userDataSource.getUserDetail(sessionId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toAccountDetails())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }
  // endregion AUTH

  // region PREF
  override suspend fun saveUserPref(userModel: UserModel) = pref.saveUser(userModel)

  override suspend fun saveRegionPref(region: String) = pref.saveRegion(region)

  override fun getUserPref(): Flow<UserModel> = pref.getUser()

  override fun getUserRegionPref(): Flow<String> = pref.getRegion()

  override suspend fun removeUserDataPref() = pref.removeUserData()
  // endregion PREF

  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>> =
    userDataSource.getCountryCode().map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> NetworkResult.success(networkResult.data?.toCountryIP())
        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }
}
