package com.waffiq.bazz_movies.data.remote.datasource

import com.waffiq.bazz_movies.data.remote.SessionID
import com.waffiq.bazz_movies.data.remote.response.CountyAPIResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.AccountDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.AuthenticationResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.CreateSessionResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface UserDataSourceInterface {
  // CALL FUNCTION
  suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>?): NetworkResult<T> {
    try {
      val response = apiCall.invoke()
      if (response != null && response.isSuccessful) return NetworkResult.success(response.body())

      val errorBody = response?.errorBody()?.string()
      return if (response?.code() == 404) NetworkResult.error("Bad Request")
      else if (!errorBody.isNullOrEmpty()) NetworkResult.error(errorBody)
      else NetworkResult.error("Error in fetching data")
    } catch (e: HttpException) {
      return NetworkResult.error(e.message ?: "Something went wrong")
    } catch (e: SocketTimeoutException) {
      return NetworkResult.error("Connection timed out. Please try again.")
    } catch (e: UnknownHostException) {
      return NetworkResult.error("Unable to resolve server hostname. Please check your internet connection.\"")
    } catch (e: IOException) {
      return NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }

  suspend fun createToken() : Flow<NetworkResult<AuthenticationResponse>>
  suspend fun deleteSession(data: SessionID) : Flow<NetworkResult<PostRateResponse>>
  suspend fun createSessionLogin(token: String) : Flow<NetworkResult<CreateSessionResponse>>
  suspend fun getUserDetail(sessionId: String): Flow<NetworkResult<AccountDetailsResponse>>
  suspend fun getCountryCode(): Flow<NetworkResult<CountyAPIResponse>>
  suspend fun login(username: String, pass: String, token: String): NetworkResult<AuthenticationResponse>
}