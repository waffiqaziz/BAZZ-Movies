package com.waffiq.bazz_movies.data.remote.datasource

import com.waffiq.bazz_movies.data.remote.SessionIDPostModel
import com.waffiq.bazz_movies.data.remote.response.countryip.CountryIPResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.AccountDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.AuthenticationResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CreateSessionResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.remote.retrofit.CountryIPApiService
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserDataSource(
  private val tmdbApiService: TMDBApiService,
  private val countryIPApiService: CountryIPApiService
) : UserDataSourceInterface {

  override suspend fun createToken(): Flow<NetworkResult<AuthenticationResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.createToken()
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun deleteSession(data: SessionIDPostModel): Flow<NetworkResult<PostRateResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(safeApiCall {
        tmdbApiService.delSession(data.sessionID)
      })
    }.flowOn(Dispatchers.IO)

  override suspend fun createSessionLogin(token: String): Flow<NetworkResult<CreateSessionResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(safeApiCall {
        tmdbApiService.createSessionLogin(token)
      })
    }.flowOn(Dispatchers.IO)

  override suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(safeApiCall {
        tmdbApiService.getAccountDetails(sessionId)
      })
    }.flowOn(Dispatchers.IO)

  override suspend fun getCountryCode(): Flow<NetworkResult<CountryIPResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      countryIPApiService.getIP()
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun login(
    username: String, pass: String, token: String
  ): Flow<NetworkResult<AuthenticationResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCallLogin {
      tmdbApiService.login(username, pass, token)
    })
  }.flowOn(Dispatchers.IO)

  companion object {
    const val TAG = "UserDataSource"

    @Volatile
    private var instance: UserDataSource? = null

    fun getInstance(
      tmdbApiService: TMDBApiService,
      countryIPApiService: CountryIPApiService
    ): UserDataSource =
      instance ?: synchronized(this) {
        instance ?: UserDataSource(
          tmdbApiService, countryIPApiService
        )
      }
  }

}