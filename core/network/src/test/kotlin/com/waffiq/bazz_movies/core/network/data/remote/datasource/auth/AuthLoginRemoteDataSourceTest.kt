package com.waffiq.bazz_movies.core.network.data.remote.datasource.auth

import com.waffiq.bazz_movies.core.network.testutils.BaseAuthDataSourceTest
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
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response.success

class AuthLoginRemoteDataSourceTest : BaseAuthDataSourceTest() {

  @Test
  fun createToken_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockAuthApiService.createToken() },
        mockApiResponse = success(authenticationResponseDump),
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
        expectedData = authenticationResponseDump,
      )
    }

  @Test
  fun createToken_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockAuthApiService.createToken() },
        errorResponse = apiInvalidFormatErrorResponse,
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
        expectedErrorMessage = errorInvalidFormatMessage,
      )
    }

  // region createToken EDGE CASE
  @Test
  fun createToken_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockAuthApiService.createToken() },
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      )
    }

  @Test
  fun createToken_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockAuthApiService.createToken() },
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      )
    }

  @Test
  fun createToken_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockAuthApiService.createToken() },
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      )
    }

  @Test
  fun createToken_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockAuthApiService.createToken() },
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      )
    }

  @Test
  fun createToken_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockAuthApiService.createToken() },
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      )
    }

  @Test
  fun createToken_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockAuthApiService.createToken() },
        dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      )
    }
  // endregion createToken EDGE CASE

  @Test
  fun deleteSession_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        mockApiResponse = success(postResponseDump),
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
        expectedData = postResponseDump,
      )
    }

  @Test
  fun deleteSession_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        errorResponse = apiInvalidFormatErrorResponse,
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
        expectedErrorMessage = errorInvalidFormatMessage,
      )
    }

  // region deleteSession EDGE CASE
  @Test
  fun deleteSession_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      )
    }

  @Test
  fun deleteSession_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      )
    }

  @Test
  fun deleteSession_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      )
    }

  @Test
  fun deleteSession_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      )
    }

  @Test
  fun deleteSession_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      )
    }

  @Test
  fun deleteSession_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      )
    }
  // endregion deleteSession EDGE CASE

  @Test
  fun createSessionLogin_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        mockApiResponse = success(createSessionResponseDump),
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
        expectedData = createSessionResponseDump,
      )
    }

  @Test
  fun createSessionLogin_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        errorResponse = apiInvalidFormatErrorResponse,
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
        expectedErrorMessage = errorInvalidFormatMessage,
      )
    }

  // region createSessionLogin EDGE CASE
  @Test
  fun createSessionLogin_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      )
    }

  @Test
  fun createSessionLogin_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      )
    }

  @Test
  fun createSessionLogin_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      )
    }

  @Test
  fun createSessionLogin_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      )
    }

  @Test
  fun createSessionLogin_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      )
    }

  @Test
  fun createSessionLogin_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
        dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      )
    }
  // endregion createSessionLogin EDGE CASE

  @Test
  fun login_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        mockApiResponse = success(authenticationResponseDump),
        dataSourceEndpointCall = {
          authRemoteDataSource.login("username", "password", "session_id")
        },
        expectedData = authenticationResponseDump,
      )
    }

  @Test
  fun login_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        errorResponse = apiInvalidFormatErrorResponse,
        dataSourceEndpointCall = {
          authRemoteDataSource.login(
            "username",
            "password",
            "session_id",
          )
        },
        expectedErrorMessage = errorInvalidFormatMessage,
      )
    }

  // region login EDGE CASE
  @Test
  fun login_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        dataSourceEndpointCall = {
          authRemoteDataSource.login(
            "username",
            "password",
            "session_id",
          )
        },
      )
    }

  @Test
  fun login_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        dataSourceEndpointCall = {
          authRemoteDataSource.login(
            "username",
            "password",
            "session_id",
          )
        },
      )
    }

  @Test
  fun login_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        dataSourceEndpointCall = {
          authRemoteDataSource.login(
            "username",
            "password",
            "session_id",
          )
        },
      )
    }

  @Test
  fun login_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        dataSourceEndpointCall = {
          authRemoteDataSource.login(
            "username",
            "password",
            "session_id",
          )
        },
      )
    }

  @Test
  fun login_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        dataSourceEndpointCall = {
          authRemoteDataSource.login(
            "username",
            "password",
            "session_id",
          )
        },
      )
    }

  @Test
  fun login_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
        dataSourceEndpointCall = {
          authRemoteDataSource.login(
            "username",
            "password",
            "session_id",
          )
        },
      )
    }
  // endregion login EDGE CASE
}
