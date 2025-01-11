package com.waffiq.bazz_movies.core.network.utils.helpers

import android.util.Log
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import okio.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Used to perform safe network requests
 */
object SafeApiCallHelper {

  private const val ERROR_CODE = 404
  private const val TAG = "SafeApiCall"

  /**
   * Safely executes an API call, handling any exceptions and returning a wrapped result.
   * This helper function ensures that common network-related exceptions (such as timeouts,
   * host resolution failures, etc.) are caught and handled gracefully.
   *
   * @param apiCall A suspend function representing the network request (e.g., a Retrofit call).
   *                This function should return a [Response] object or null if no response is received.
   *
   * @return A [NetworkResult] wrapping the outcome of the network request:
   * - [NetworkResult.Success] if the API call is successful and the response body is not null.
   * - [NetworkResult.Error] if the API call fails due to an exception or null/unsuccessful response.
   *
   * Exception Handling:
   * - [UnknownHostException]: Indicates a failure to resolve the server's hostname, likely due to network issues.
   * - [SocketTimeoutException]: Indicates the request timed out, suggesting a retry might be needed.
   * - [HttpException]: Indicates a non-2xx HTTP response, captured as a general HTTP error.
   * - [IOException]: Captures broader network-related issues, such as disconnections.
   * - [Exception]: Catches any unexpected errors, preventing crashes and returning an "Unknown error" message.
   */
  suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>?): NetworkResult<T> =
    performApiCall(apiCall)

  private fun <T> processApiResponse(response: retrofit2.Response<T>): NetworkResult<T> {
    return when {
      false -> NetworkResult.Error("No response received from the server")

      response.isSuccessful -> {
        val responseBody = response.body()
        if (responseBody != null) {
          NetworkResult.Success(responseBody)
        } else {
          NetworkResult.Error("Response received but body is null")
        }
      }

      else -> {
        handleErrorBody(response)
      }
    }
  }

  private fun <T> handleErrorBody(response: retrofit2.Response<T>): NetworkResult.Error {
    val errorBody = response.errorBody()?.string()
    return when {
      response.code() == ERROR_CODE -> NetworkResult.Error("Invalid request (400)")
      !errorBody.isNullOrEmpty() -> parseErrorBody(errorBody)
      else -> NetworkResult.Error("Server error: No error details provided")
    }
  }

  private fun parseErrorBody(errorBody: String): NetworkResult.Error {
    return try {
      val errorMessage =
        JSONObject(errorBody).optString("status_message", "Error details not available")
      NetworkResult.Error(errorMessage)
    } catch (e: JSONException) {
      Log.e(TAG, "An error occurred:  ${e.message}")
      NetworkResult.Error("Malformed error response from the server")
    }
  }

  private suspend fun <T> performApiCall(apiCall: suspend () -> retrofit2.Response<T>?): NetworkResult<T> {
    return try {
      val response: retrofit2.Response<T>? = apiCall()
      if (response != null) {
        processApiResponse(response)
      } else {
        NetworkResult.Error("No response from server")
      }
    } catch (e: UnknownHostException) {
      Log.e(TAG, "An error occurred: ${e.message}", e)
      NetworkResult.Error("Unable to resolve server hostname. Please check your internet connection.")
    } catch (e: SocketTimeoutException) {
      Log.e(TAG, "An error occurred: ${e.message}", e)
      NetworkResult.Error("Connection timed out. Please try again.")
    } catch (e: HttpException) {
      Log.e(TAG, "An error occurred: ${e.message}", e)
      NetworkResult.Error("Something went wrong")
    } catch (e: IOException) {
      Log.e(TAG, "An error occurred: ${e.message}", e)
      NetworkResult.Error("Please check your network connection")
    } catch (e: Exception) {
      Log.e(TAG, "An error occurred: ${e.message}", e)
      NetworkResult.Error("An unknown error occurred")
    }
  }
}
