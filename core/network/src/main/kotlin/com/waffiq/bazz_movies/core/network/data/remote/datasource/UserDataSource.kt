package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.CountryIPApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.safeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(
  private val tmdbApiService: TMDBApiService,
  private val countryIPApiService: CountryIPApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserDataSourceInterface {

  override suspend fun createToken(): Flow<NetworkResult<AuthenticationResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.createToken()
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun deleteSession(sessionId: String): Flow<NetworkResult<PostResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.delSession(sessionId)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun createSessionLogin(sessionId: String): Flow<NetworkResult<CreateSessionResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.createSessionLogin(sessionId)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getAccountDetails(sessionId)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        countryIPApiService.getIP()
      }
    )
  }.flowOn(ioDispatcher)

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
  }.flowOn(ioDispatcher)
}
