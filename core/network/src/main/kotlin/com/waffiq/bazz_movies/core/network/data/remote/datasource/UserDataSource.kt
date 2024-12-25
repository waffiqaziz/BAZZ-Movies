package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.CountryIPApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.safeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(
  private val tmdbApiService: TMDBApiService,
  private val countryIPApiService: CountryIPApiService
) : UserDataSourceInterface {

  override suspend fun createToken(): Flow<NetworkResult<AuthenticationResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.createToken()
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun deleteSession(sessionId: String): Flow<NetworkResult<PostResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.delSession(sessionId)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun createSessionLogin(requestToken: String): Flow<NetworkResult<CreateSessionResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.createSessionLogin(requestToken)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getAccountDetails(sessionId)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        countryIPApiService.getIP()
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun login(
    username: String,
    pass: String,
    sessionId: String
  ): Flow<NetworkResult<AuthenticationResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.login(username, pass, sessionId)
      }
    )
  }.flowOn(Dispatchers.IO)
}
