package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface UserDataSourceInterface {
  fun createToken(): Flow<NetworkResult<AuthenticationResponse>>
  fun deleteSession(sessionId: String): Flow<NetworkResult<PostResponse>>
  fun createSessionLogin(sessionId: String): Flow<NetworkResult<CreateSessionResponse>>
  fun getAccountDetails(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>>
  fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>>
  fun login(
    username: String,
    pass: String,
    sessionId: String
  ): Flow<NetworkResult<AuthenticationResponse>>
}
