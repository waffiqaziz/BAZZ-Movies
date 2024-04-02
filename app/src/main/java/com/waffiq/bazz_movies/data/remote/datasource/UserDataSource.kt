package com.waffiq.bazz_movies.data.remote.datasource

import com.waffiq.bazz_movies.data.remote.SessionID
import com.waffiq.bazz_movies.data.remote.response.CountyAPIResponse
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
import okio.IOException

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

  override suspend fun deleteSession(data: SessionID): Flow<NetworkResult<PostRateResponse>> =
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

  override suspend fun getCountryCode(): Flow<NetworkResult<CountyAPIResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      countryIPApiService.getIP()
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun login(
    username: String,
    pass: String,
    token: String
  ): NetworkResult<AuthenticationResponse> {
    return try {
      val response = tmdbApiService.login(username, pass, token)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) NetworkResult.success(responseBody)
        else NetworkResult.error("Error in fetching data")
      } else {
        val errorMessage = response.message() ?: "Unknown error"
        NetworkResult.error(errorMessage)
      }
    } catch (e: IOException) {
      NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      NetworkResult.error("Something went wrong")
    }
  }

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