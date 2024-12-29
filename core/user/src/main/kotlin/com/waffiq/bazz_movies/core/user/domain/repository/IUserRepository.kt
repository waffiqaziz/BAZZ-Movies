package com.waffiq.bazz_movies.core.user.domain.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.UserModel
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
  ): Flow<Outcome<Authentication>>

  suspend fun createToken(): Flow<Outcome<Authentication>>
  suspend fun deleteSession(sessionId: String): Flow<Outcome<Post>>
  suspend fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>>
  suspend fun getUserDetail(sessionId: String): Flow<Outcome<AccountDetails>>

  suspend fun saveUserPref(userModel: UserModel)
  suspend fun saveRegionPref(region: String)
  fun getUserPref(): Flow<UserModel>
  fun getUserToken(): Flow<String>
  fun getUserRegionPref(): Flow<String>
  suspend fun removeUserDataPref()

  suspend fun getCountryCode(): Flow<Outcome<CountryIP>>
}
