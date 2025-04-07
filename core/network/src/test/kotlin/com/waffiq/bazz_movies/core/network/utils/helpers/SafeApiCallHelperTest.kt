package com.waffiq.bazz_movies.core.network.utils.helpers

import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.safeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SafeApiCallHelperTest {

  @Test
  fun safeApiCall_shouldSuccessResponseWithBody() = runTest {
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
  fun safeApiCall_shouldSuccessResponseWithNullBody() = runTest {
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
  fun safeApiCall_shouldSuccessResponseWithMissingCode() = runTest {
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
  fun safeApiCall_shouldErrorBodyNullResponseWithMessageNoDetailProvided() = runTest {
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
  fun safeApiCall_shouldError404ResponseInvalidRequest() = runTest {
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
  fun safeApiCall_shouldErrorResponseWithStatusMessage() = runTest {
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
  fun safeApiCall_shouldErrorResponseWithoutStatusMessage() = runTest {
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
  fun safeApiCall_shouldErrorResponseWithEmptyErrorBody() = runTest {
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
  fun safeApiCall_shouldErrorResponseWithNullErrorBody() = runTest {
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
  fun safeApiCall_shouldErrorResponseWithMalformedJson() = runTest {
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
  fun safeApiCall_shouldErrorResponseWithPlainTextErrorBody() = runTest {
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
  fun safeApiCall_shouldNullResponseShouldResponseWithoutException() = runTest {
    val apiCall: Response<String>? = null
    val result = safeApiCall { apiCall }

    assert(result is NetworkResult.Error)
    assertEquals(
      "No response from server",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_shouldHandlesUnknownHostException() = runTest {
    val result = safeApiCall<String> { throw UnknownHostException("Host not found") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Unable to resolve server hostname. Please check your internet connection.",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_shouldHandlesSocketTimeoutException() = runTest {
    val result = safeApiCall<String> { throw SocketTimeoutException("Timeout") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Connection timed out. Please try again.",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_shouldHandlesHttpException() = runTest {
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
  fun safeApiCall_shouldHandlesIOException() = runTest {
    val result = safeApiCall<String> { throw IOException("Network error") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "Please check your network connection",
      (result as NetworkResult.Error).message
    )
  }

  @Test
  fun safeApiCall_shouldHandlesUnknownException() = runTest {
    val result = safeApiCall<String> { throw Exception("Unexpected error") }
    assert(result is NetworkResult.Error)
    assertEquals(
      "An unknown error occurred",
      (result as NetworkResult.Error).message
    )
  }
}
