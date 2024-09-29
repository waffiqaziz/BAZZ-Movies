package com.waffiq.bazz_movies.utils.helpers

import android.util.Log
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Used to perform safe network requests
 */
object SafeApiCallHelper {

  /**
   * Safely executes an API call, handling any exceptions and returning a wrapped result.
   * This helper function ensures that common network-related exceptions (such as timeouts,
   * host resolution failures, etc.) are caught and handled gracefully.
   *
   * @param apiCall A suspend function representing the network request (e.g., a Retrofit call).
   *                This function should return a [Response] object or null if no response is received.
   *
   * @return A [NetworkResult] wrapping the outcome of the network request:
   *         - [NetworkResult.Success] if the API call is successful and the response body is not null.
   *         - [NetworkResult.Error] if the API call fails due to an exception or null/unsuccessful response.
   *
   * Exception Handling:
   * - [UnknownHostException]: Indicates a failure to resolve the server's hostname, likely due to network issues.
   * - [SocketTimeoutException]: Indicates the request timed out, suggesting a retry might be needed.
   * - [HttpException]: Indicates a non-2xx HTTP response, captured as a general HTTP error.
   * - [IOException]: Captures broader network-related issues, such as disconnections.
   * - [Exception]: Catches any unexpected errors, preventing crashes and returning an "Unknown error" message.
   */
  suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>?): NetworkResult<T> =
    performApiCall(apiCall)

  private fun <T> processApiResponse(response: Response<T>?): NetworkResult<T> {
    return if (response != null && response.isSuccessful) {
      // if the response is successful but the body is null, return Error
      response.body()?.let {
        NetworkResult.Success(it)
      } ?: NetworkResult.Error("Response body is null")
    } else {
      NetworkResult.Error(response?.errorBody()?.string() ?: "Unknown error")
    }
  }

  private suspend fun <T> performApiCall(apiCall: suspend () -> Response<T>?): NetworkResult<T> {
    return try {
      val response : Response<T>? = apiCall()
      if (response != null) {
        processApiResponse(response)
      } else {
        NetworkResult.Error("Received null response")
      }
    } catch (e: UnknownHostException) {
      Log.e("SafeApiCall", "An error occurred: ${e.message}", e)
      NetworkResult.Error("Unable to resolve server hostname. Please check your internet connection.")
    } catch (e: SocketTimeoutException) {
      Log.e("SafeApiCall", "An error occurred: ${e.message}", e)
      NetworkResult.Error("Connection timed out. Please try again.")
    } catch (e: HttpException) {
      Log.e("SafeApiCall", "An error occurred: ${e.message}", e)
      NetworkResult.Error("Something went wrong")
    } catch (e: IOException) {
      Log.e("SafeApiCall", "An error occurred: ${e.message}", e)
      NetworkResult.Error("Please check your network connection")
    } catch (e: Exception) {
      Log.e("SafeApiCall", "An error occurred: ${e.message}", e)
      NetworkResult.Error("Unknown error")
    }
  }
}
