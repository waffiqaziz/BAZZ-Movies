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
  fun createToken_returnExpectedResponse() = runTest {
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
  fun createToken_returnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.createToken() },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region createToken EDGE CASE
  @Test
  fun createToken_returnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_returnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }
  // endregion createToken EDGE CASE

  @Test
  fun deleteSession_returnExpectedResponse() = runTest {
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
  fun deleteSession_returnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region deleteSession EDGE CASE
  @Test
  fun deleteSession_returnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_returnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }
  // endregion deleteSession EDGE CASE

  @Test
  fun createSessionLogin_returnExpectedResponse() = runTest {
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
  fun createSessionLogin_returnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region createSessionLogin EDGE CASE
  @Test
  fun createSessionLogin_returnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_returnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }
  // endregion createSessionLogin EDGE CASE

  @Test
  fun login_returnExpectedResponse() = runTest {
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
  fun login_returnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region login EDGE CASE
  @Test
  fun login_returnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_returnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }
  // endregion login EDGE CASE
}
