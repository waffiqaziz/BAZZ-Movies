package com.waffiq.bazz_movies.core.data.remote.datasource

import com.waffiq.bazz_movies.core.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.core.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface UserDataSourceInterface {
  suspend fun createToken(): Flow<NetworkResult<AuthenticationResponse>>
  suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<PostResponse>>
  suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSessionResponse>>
  suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>>
  suspend fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>>
  suspend fun login(
    username: String,
    pass: String,
    token: String
  ): Flow<NetworkResult<AuthenticationResponse>>
}
