package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface UserDataSourceInterface {
  suspend fun createToken(): Flow<NetworkResult<AuthenticationResponse>>
  suspend fun deleteSession(sessionId: String): Flow<NetworkResult<PostResponse>>
  suspend fun createSessionLogin(sessionId: String): Flow<NetworkResult<CreateSessionResponse>>
  suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>>
  suspend fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>>
  suspend fun login(
    username: String,
    pass: String,
    sessionId: String
  ): Flow<NetworkResult<AuthenticationResponse>>
}
