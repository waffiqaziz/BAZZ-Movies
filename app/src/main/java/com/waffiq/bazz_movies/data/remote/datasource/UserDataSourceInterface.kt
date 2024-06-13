package com.waffiq.bazz_movies.data.remote.datasource

import com.waffiq.bazz_movies.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface UserDataSourceInterface {

  // CALL FUNCTION
  suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>?): NetworkResult<T> {
    try {
      val response = apiCall()
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
      return NetworkResult.error("Unable to resolve server hostname. Please check your internet connection.")
    } catch (e: IOException) {
      return NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      return NetworkResult.error(e.toString())
    }
  }

  suspend fun <T> safeApiCallLogin(apiCall: suspend () -> Response<T>?): NetworkResult<T> {
    try {
      val response = apiCall()
      if (response != null) {
        return if (response.isSuccessful) {
          NetworkResult.success(response.body())
        } else {
          val errorBody = response.errorBody()?.string()
          if (response.code() == 404) {
            NetworkResult.error("Bad Request")
          } else if (!errorBody.isNullOrEmpty()) {
            return NetworkResult.error(
              errorBody.let {
                try {
                  JSONObject(it).getString("status_message")
                } catch (e: JSONException) {
                  "Error parsing response"
                }
              } ?: "Error in fetching data"
            )
          } else {
            NetworkResult.error("Error in fetching data")
          }
        }
      } else {
        return NetworkResult.error("Null response")
      }

    } catch (e: HttpException) {
      return NetworkResult.error(e.message ?: "Something went wrong")
    } catch (e: SocketTimeoutException) {
      return NetworkResult.error("Connection timed out. Please try again.")
    } catch (e: UnknownHostException) {
      return NetworkResult.error("Unable to resolve server hostname. Please check your internet connection.")
    } catch (e: IOException) {
      return NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      return NetworkResult.error(e.toString())
    }
  }

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