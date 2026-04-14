package com.waffiq.bazz_movies.core.network.data.remote.datasource.auth

import com.waffiq.bazz_movies.core.network.testutils.BaseAuthDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class AuthLoginRemoteDataSourceTest : BaseAuthDataSourceTest() {

  @Test
  fun createToken_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockAuthApiService.createToken() },
      mockApiResponse = Response.success(DataDumpManager.authenticationResponseDump),
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      expectedData = DataDumpManager.authenticationResponseDump,
    )
  }

  @Test
  fun createToken_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockAuthApiService.createToken() },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region createToken EDGE CASE
  @Test
  fun createToken_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockAuthApiService.createToken() },
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockAuthApiService.createToken() },
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockAuthApiService.createToken() },
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockAuthApiService.createToken() },
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockAuthApiService.createToken() },
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
    )
  }

  @Test
  fun createToken_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockAuthApiService.createToken() },
      dataSourceEndpointCall = { authRemoteDataSource.createToken() },
    )
  }
  // endregion createToken EDGE CASE

  @Test
  fun deleteSession_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      mockApiResponse = Response.success(DataDumpManager.postResponseDump),
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      expectedData = DataDumpManager.postResponseDump,
    )
  }

  @Test
  fun deleteSession_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region deleteSession EDGE CASE
  @Test
  fun deleteSession_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
    )
  }

  @Test
  fun deleteSession_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockAuthApiService.deleteSession("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.deleteSession("session_id") },
    )
  }
  // endregion deleteSession EDGE CASE

  @Test
  fun createSessionLogin_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      mockApiResponse = Response.success(DataDumpManager.createSessionResponseDump),
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      expectedData = DataDumpManager.createSessionResponseDump,
    )
  }

  @Test
  fun createSessionLogin_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region createSessionLogin EDGE CASE
  @Test
  fun createSessionLogin_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
    )
  }

  @Test
  fun createSessionLogin_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockAuthApiService.createSessionLogin("request_token") },
      dataSourceEndpointCall = { authRemoteDataSource.createSessionLogin("request_token") },
    )
  }
  // endregion createSessionLogin EDGE CASE

  @Test
  fun login_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      mockApiResponse = Response.success(DataDumpManager.authenticationResponseDump),
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
      expectedData = DataDumpManager.authenticationResponseDump,
    )
  }

  @Test
  fun login_whenApiInvalidFormat_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
      expectedErrorMessage = errorInvalidFormatMessage
    )
  }

  // region login EDGE CASE
  @Test
  fun login_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
    )
  }

  @Test
  fun login_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockAuthApiService.login("username", "password", "session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.login("username", "password", "session_id") },
    )
  }
  // endregion login EDGE CASE
}