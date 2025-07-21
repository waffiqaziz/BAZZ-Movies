package com.waffiq.bazz_movies.core.network.utils.helpers

import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.safeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SafeApiCallHelperTest {

  private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

  @Test
  fun safeApiCall_whenSuccessful_returnsResponseWithBody() = runTest {
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns true
      every { code() } returns 200
      every { body() } returns "Success"
    }

    val result = safeApiCall { mockResponse }
    assert(result is NetworkResult.Success)
    assertEquals("Success", (result as NetworkResult.Success).data)
  }

  @Test
  fun safeApiCall_whenSuccessful_returnsResponseWithNullBody() = runTest {
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns true
      every { body() } returns null
    }

    val result = safeApiCall { mockResponse }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Response received but body is null",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenSuccessful_returnsResponseWithMissingCode() = runTest {
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns true
      every { code() } returns 0
      every { body() } returns "Success"
    }

    val result = safeApiCall { mockResponse }
    assert(result is NetworkResult.Success)
    assertEquals("Success", (result as NetworkResult.Success).data)
  }

  @Test
  fun safeApiCall_whenUnSuccessfulAndBodyIsNull_returnsErrorWithMessageNoDetailProvided() =
    runTest {
      val mockResponse = mockk<Response<String>> {
        every { isSuccessful } returns false
        every { code() } returns 500
        every { body() } returns null
        every { errorBody() } returns null
      }

      val result = safeApiCall { mockResponse }
      assert(result is NetworkResult.Error)
      assertEquals(
        "Server error: No error details provided",
        (result as NetworkResult.Error).message
      )
    }

  @Test
  fun safeApiCall_whenUnsuccessful_returnsErrorWith404AndInvalidRequestMessage() = runTest {
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
      every { code() } returns 404
      every { body() } returns null
      every { errorBody() } returns null
    }
    val result = safeApiCall { mockResponse }

    assert(result is NetworkResult.Error)
    assertEquals(
      "Invalid request (400)",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_returnsErrorResponseWithStatusMessage() = runTest {
    val errorString = """
      {
          "status_code": 7,
          "status_message": "Invalid API key: You must be granted a valid key.",
          "success": false
      }
    """
    val mockResponseBody = mockk<ResponseBody> {
      every { string() } returns errorString
    }
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
      every { code() } returns 401
      every { errorBody() } returns mockResponseBody
    }
    val result = safeApiCall { mockResponse }

    assert(result is NetworkResult.Error)
    assertEquals(
      "Invalid API key: You must be granted a valid key.",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_returnsErrorResponseWithoutStatusMessage() = runTest {
    val errorString = """
      {
          "status_code": 7,
          "success": false
      }
    """
    val mockResponseBody = mockk<ResponseBody> {
      every { string() } returns errorString
    }
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
      every { code() } returns 401
      every { errorBody() } returns mockResponseBody
    }
    val result = safeApiCall { mockResponse }

    assert(result is NetworkResult.Error)
    assertEquals(
      "Error details not available",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_returnErrorResponseWithEmptyErrorBody() = runTest {
    val mockResponseBody = mockk<ResponseBody> {
      every { string() } returns ""
    }
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
      every { code() } returns 500
      every { errorBody() } returns mockResponseBody
    }

    val result = safeApiCall { mockResponse }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Server error: No error details provided",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_returnsErrorResponseWithNullErrorBody() = runTest {
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
      every { code() } returns 400
      every { errorBody() } returns null
    }

    val result = safeApiCall { mockResponse }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Server error: No error details provided",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_returnsErrorResponseWithMalformedJson() = runTest {
    val errorString = """{invalid_json}"""
    val mockResponseBody = mockk<ResponseBody> {
      every { string() } returns errorString
    }
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
      every { code() } returns 400
      every { errorBody() } returns mockResponseBody
    }
    val result = safeApiCall { mockResponse }

    assert(result is NetworkResult.Error)
    assertEquals(
      "Malformed error response from the server",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_returnsErrorResponseWithPlainTextBody() = runTest {
    val errorString = "Server error occurred"
    val mockResponseBody = mockk<ResponseBody> {
      every { string() } returns errorString
    }
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
      every { code() } returns 500
      every { errorBody() } returns mockResponseBody
    }

    val result = safeApiCall { mockResponse }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Malformed error response from the server",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_returnNullResponse() = runTest {
    val apiCall: Response<String>? = null
    val result = safeApiCall { apiCall }

    assert(result is NetworkResult.Error)
    assertEquals(
      "No response from server",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_shouldHandleUnknownHostException() = runTest {
    val result = safeApiCall<String> { throw UnknownHostException("Host not found") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Unable to resolve server hostname. Please check your internet connection.",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_shouldHandleSocketTimeoutException() = runTest {
    val result = safeApiCall<String> { throw SocketTimeoutException("Timeout") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Connection timed out. Please try again.",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_whenUnsuccessful_shouldHandleHttpException() = runTest {
    val exception = mockk<HttpException> {
      every { message } returns "HTTP Error"
    }
    val result = safeApiCall<String> { throw exception }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Something went wrong",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall__whenUnsuccessful_shouldHandleIOException() = runTest {
    val result = safeApiCall<String> { throw IOException("Network error") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Please check your network connection",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  @Suppress("TooGenericExceptionThrown")
  fun safeApiCall_whenUnsuccessful_shouldHandleUnknownException() = runTest {
    val result = safeApiCall<String> { throw Exception("Unexpected error") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "An unknown error occurred",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun executeApiCall_whenApiCallSucceeds_emitsLoadingThenSuccess() = runTest {
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns true
      every { body() } returns "data"
    }
    val apiCall: suspend () -> Response<String> = { mockResponse }

    val result = executeApiCall(apiCall, testDispatcher).toList()

    assertEquals(2, result.size)
    assertTrue(result[0] is NetworkResult.Loading)
    assertTrue(result[1] is NetworkResult.Success)
  }

  @Test
  fun executeApiCall_whenApiCallFails_emitsLoadingThenError() = runTest {
    val mockResponse = mockk<Response<String>> {
      every { isSuccessful } returns false
    }
    val apiCall: suspend () -> Response<String> = { mockResponse }

    val result = executeApiCall(apiCall, testDispatcher).toList()

    assertEquals(2, result.size)
    assertTrue(result[0] is NetworkResult.Loading)
    assertTrue(result[1] is NetworkResult.Error)
  }

  @Test
  fun executeApiCall_whenExceptionThrown_emitsLoadingThenError() = runTest {
    val apiCall: suspend () -> Response<String> = { throw Exception() }

    val result = executeApiCall(apiCall, testDispatcher).toList()

    assertEquals(2, result.size)
    assertTrue(result[0] is NetworkResult.Loading)
    assertTrue(result[1] is NetworkResult.Error)
  }
}
