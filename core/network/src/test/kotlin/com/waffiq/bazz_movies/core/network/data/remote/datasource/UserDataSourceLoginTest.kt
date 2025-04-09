package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseUserDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.authenticationResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.createSessionResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.postResponseDump
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class UserDataSourceLoginTest : BaseUserDataSourceTest() {

  @Test
  fun createToken_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      mockApiResponse = Response.success(authenticationResponseDump),
      dataSourceEndpointCall = { userDataSource.createToken() },
      expectedData = authenticationResponseDump,
    ) { data ->
      assertTrue(data.success)
      assertEquals("expire_date", data.expireAt)
      assertEquals("request_token", data.requestToken)
    }
  }

  @Test
  fun createToken_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.createToken() },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region createToken EDGE CASE
  @Test
  fun createToken_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }
  // endregion createToken EDGE CASE

  @Test
  fun deleteSession_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      mockApiResponse = Response.success(postResponseDump),
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
      expectedData = postResponseDump,
    ) { data ->
      assertTrue(data.success == true)
      assertEquals(200, data.statusCode)
      assertEquals("Success", data.statusMessage)
    }
  }

  @Test
  fun deleteSession_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region deleteSession EDGE CASE
  @Test
  fun deleteSession_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }
  // endregion deleteSession EDGE CASE

  @Test
  fun createSessionLogin_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      mockApiResponse = Response.success(createSessionResponseDump),
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
      expectedData = createSessionResponseDump,
    ) { data ->
      assertTrue(data.success)
      assertEquals("session_id", data.sessionId)
    }
  }

  @Test
  fun createSessionLogin_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region createSessionLogin EDGE CASE
  @Test
  fun createSessionLogin_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }
  // endregion createSessionLogin EDGE CASE

  @Test
  fun login_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      mockApiResponse = Response.success(authenticationResponseDump),
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
      expectedData = authenticationResponseDump,
    ) { data ->
      assertEquals("expire_date", data.expireAt)
      assertEquals("request_token", data.requestToken)
    }
  }

  @Test
  fun login_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region login EDGE CASE
  @Test
  fun login_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }
  // endregion login EDGE CASE
}
