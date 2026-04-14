package com.waffiq.bazz_movies.core.network.data.remote.datasource.auth

import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.AuthApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
  private val authApiService: AuthApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthRemoteDataSourceInterface {

  override fun createToken(): Flow<NetworkResult<AuthenticationResponse>> =
    executeApiCall(
      apiCall = { authApiService.createToken() },
      ioDispatcher = ioDispatcher,
    )

  override fun deleteSession(sessionId: String): Flow<NetworkResult<PostResponse>> =
    executeApiCall(
      apiCall = { authApiService.deleteSession(sessionId) },
      ioDispatcher = ioDispatcher,
    )

  override fun createSessionLogin(sessionId: String): Flow<NetworkResult<CreateSessionResponse>> =
    executeApiCall(
      apiCall = { authApiService.createSessionLogin(sessionId) },
      ioDispatcher = ioDispatcher,
    )

  override fun getAccountDetails(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>> =
    executeApiCall(
      apiCall = { authApiService.getAccountDetails(sessionId) },
      ioDispatcher = ioDispatcher,
    )

  override fun login(
    username: String,
    pass: String,
    sessionId: String,
  ): Flow<NetworkResult<AuthenticationResponse>> =
    executeApiCall(
      apiCall = { authApiService.login(username, pass, sessionId) },
      ioDispatcher = ioDispatcher,
    )
}
