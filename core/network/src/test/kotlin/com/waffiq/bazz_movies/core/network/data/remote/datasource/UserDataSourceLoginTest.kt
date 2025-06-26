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
  fun createToken_whenSuccessful_returnsExpectedResponse() = runTest {
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
  fun createToken_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.createToken() },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region createToken EDGE CASE
  @Test
  fun createToken_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.createToken() },
      dataSourceEndpointCall = { userDataSource.createToken() },
    )
  }
  // endregion createToken EDGE CASE

  @Test
  fun deleteSession_whenSuccessful_returnsExpectedResponse() = runTest {
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
  fun deleteSession_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region deleteSession EDGE CASE
  @Test
  fun deleteSession_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.delSession("session_id") },
      dataSourceEndpointCall = { userDataSource.deleteSession("session_id") },
    )
  }
  // endregion deleteSession EDGE CASE

  @Test
  fun createSessionLogin_whenSuccessful_returnsExpectedResponse() = runTest {
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
  fun createSessionLogin_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region createSessionLogin EDGE CASE
  @Test
  fun createSessionLogin_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { userDataSource.createSessionLogin("request_token") },
    )
  }
  // endregion createSessionLogin EDGE CASE

  @Test
  fun login_whenSuccessful_returnsExpectedResponse() = runTest {
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
  fun login_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region login EDGE CASE
  @Test
  fun login_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { userDataSource.login("username", "password", "session_id") },
    )
  }
  // endregion login EDGE CASE
}
