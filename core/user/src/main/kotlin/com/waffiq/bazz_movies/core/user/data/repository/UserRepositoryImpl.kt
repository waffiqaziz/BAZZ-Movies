package com.waffiq.bazz_movies.core.user.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPostResult
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
  override fun createToken(): Flow<Outcome<Authentication>> =
    userDataSource.createToken().toOutcome { it.toAuthentication() }

  override fun login(
    username: String,
    pass: String,
    sessionId: String,
  ): Flow<Outcome<Authentication>> =
    userDataSource.login(username, pass, sessionId).toOutcome { it.toAuthentication() }

  override fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>> =
    userDataSource.createSessionLogin(requestToken).toOutcome { it.toCreateSession() }

  override fun deleteSession(sessionId: String): Flow<Outcome<PostResult>> =
    userDataSource.deleteSession(sessionId).toOutcome { it.toPostResult() }

  override fun getAccountDetails(sessionId: String): Flow<Outcome<AccountDetails>> =
    userDataSource.getAccountDetails(sessionId).toOutcome { it.toAccountDetails() }
  // endregion AUTH

  // region PREF
  override suspend fun saveUserPref(userModel: UserModel) =
    pref.saveUser(userModel.toUserModelPref())

  override suspend fun saveRegionPref(region: String) = pref.saveRegion(region)

  override fun getUserPref(): Flow<UserModel> = pref.getUser().map { it.toUserModel() }

  override fun getUserToken(): Flow<String> = pref.getToken()

  override fun getUserRegionPref(): Flow<String> = pref.getRegion()

  override suspend fun savePermissionAsked() = pref.savePermissionAsked()

  override fun getPermissionAsked(): Flow<Boolean> = pref.getPermissionAsked()

  override suspend fun removeUserDataPref() = pref.removeUserData()
  // endregion PREF

  override fun getCountryCode(): Flow<Outcome<CountryIP>> =
    userDataSource.getCountryCode().toOutcome { it.toCountryIP() }
}
