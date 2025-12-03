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
  fun login(
    username: String,
    pass: String,
    sessionId: String,
  ): Flow<Outcome<Authentication>>

  fun createToken(): Flow<Outcome<Authentication>>
  fun deleteSession(sessionId: String): Flow<Outcome<Post>>
  fun createSessionLogin(requestToken: String): Flow<Outcome<CreateSession>>
  fun getUserDetail(sessionId: String): Flow<Outcome<AccountDetails>>

  suspend fun saveUserPref(userModel: UserModel)
  suspend fun saveRegionPref(region: String)
  fun getUserPref(): Flow<UserModel>
  fun getUserToken(): Flow<String>
  fun getUserRegionPref(): Flow<String>
  suspend fun removeUserDataPref()

  fun getCountryCode(): Flow<Outcome<CountryIP>>
}
