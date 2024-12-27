package com.waffiq.bazz_movies.core.user.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.network.data.remote.datasource.UserDataSource
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAccountDetails
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAuthentication
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCountryIP
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCreateSession
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toUserModel
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toUserModelPref
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
  override suspend fun createToken(): Flow<Outcome<Authentication>> =
    userDataSource.createToken().map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toAuthentication())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun login(
    username: String,
    pass: String,
    token: String
  ): Flow<Outcome<Authentication>> =
    userDataSource.login(username, pass, token).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toAuthentication())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>> =
    userDataSource.createSessionLogin(requestToken).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toCreateSession())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun deleteSession(sessionId: String): Flow<Outcome<Post>> =
    userDataSource.deleteSession(sessionId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toPost())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getUserDetail(sessionId: String): Flow<Outcome<AccountDetails>> =
    userDataSource.getUserDetail(sessionId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toAccountDetails())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }
  // endregion AUTH

  // region PREF
  override suspend fun saveUserPref(userModel: UserModel) =
    pref.saveUser(userModel.toUserModelPref())

  override suspend fun saveRegionPref(region: String) = pref.saveRegion(region)

  override fun getUserPref(): Flow<UserModel> = pref.getUser().map { it.toUserModel() }

  override fun getUserToken(): Flow<String> = pref.getToken()

  override fun getUserRegionPref(): Flow<String> = pref.getRegion()

  override suspend fun removeUserDataPref() = pref.removeUserData()
  // endregion PREF

  override suspend fun getCountryCode(): Flow<Outcome<CountryIP>> =
    userDataSource.getCountryCode().map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toCountryIP())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }
}
