package com.waffiq.bazz_movies.core.user.domain.repository

import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.data.model.UserModel
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
  suspend fun login(
    username: String,
    pass: String,
    sessionId: String
  ): Flow<NetworkResult<Authentication>>

  suspend fun createToken(): Flow<NetworkResult<Authentication>>
  suspend fun deleteSession(sessionId: String): Flow<NetworkResult<Post>>
  suspend fun createSessionLogin(requestToken: String): Flow<NetworkResult<CreateSession>>
  suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>>

  suspend fun saveUserPref(userModel: UserModel)
  suspend fun saveRegionPref(region: String)
  fun getUserPref(): Flow<UserModel>
  fun getUserToken(): Flow<String>
  fun getUserRegionPref(): Flow<String>
  suspend fun removeUserDataPref()

  suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>>
}
