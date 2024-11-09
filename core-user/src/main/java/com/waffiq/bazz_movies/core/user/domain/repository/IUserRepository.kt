package com.waffiq.bazz_movies.core.user.domain.repository

import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.network.data.remote.post_body.SessionIDPostModel
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
    token: String
  ): Flow<NetworkResult<Authentication>>

  suspend fun createToken(): Flow<NetworkResult<Authentication>>
  suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<Post>>
  suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSession>>
  suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetails>>

  suspend fun saveUserPref(userModel: UserModel)
  suspend fun saveRegionPref(region: String)
  fun getUserPref(): Flow<UserModel>
  fun getUserToken(): Flow<String>
  fun getUserRegionPref(): Flow<String>
  suspend fun removeUserDataPref()

  suspend fun getCountryCode(): Flow<NetworkResult<CountryIP>>
}
