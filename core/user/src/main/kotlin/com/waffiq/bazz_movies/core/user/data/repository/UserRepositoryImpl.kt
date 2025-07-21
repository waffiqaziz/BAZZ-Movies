package com.waffiq.bazz_movies.core.user.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.network.data.remote.datasource.UserDataSource
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
class UserRepositoryImpl @Inject constructor(
  private val pref: UserPreference,
  private val userDataSource: UserDataSource,
) : IUserRepository {

  // region AUTH
  override suspend fun createToken(): Flow<Outcome<Authentication>> =
    userDataSource.createToken().toOutcome { it.toAuthentication() }

  override suspend fun login(
    username: String,
    pass: String,
    sessionId: String,
  ): Flow<Outcome<Authentication>> =
    userDataSource.login(username, pass, sessionId).toOutcome { it.toAuthentication() }

  override suspend fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>> =
    userDataSource.createSessionLogin(requestToken).toOutcome { it.toCreateSession() }

  override suspend fun deleteSession(sessionId: String): Flow<Outcome<Post>> =
    userDataSource.deleteSession(sessionId).toOutcome { it.toPost() }

  override suspend fun getUserDetail(sessionId: String): Flow<Outcome<AccountDetails>> =
    userDataSource.getUserDetail(sessionId).toOutcome { it.toAccountDetails() }
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
    userDataSource.getCountryCode().toOutcome { it.toCountryIP() }
}
