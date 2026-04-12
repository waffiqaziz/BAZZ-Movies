package com.waffiq.bazz_movies.core.network.data.remote.datasource.auth

import com.waffiq.bazz_movies.core.network.testutils.BaseAuthDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class AuthAccountDetailsRemoteDataSourceTest : BaseAuthDataSourceTest() {

  @Test
  fun getAccountDetails_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      mockApiResponse = Response.success(DataDumpManager.accountDetailsResponse),
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      expectedData = DataDumpManager.accountDetailsResponse,
    )
  }

  @Test
  fun getAccountDetails_whenSuccessful_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region getAccountDetails EDGE CASE
  @Test
  fun getAccountDetails_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
    )
  }

  @Test
  fun getAccountDetails_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
    )
  }

  @Test
  fun getAccountDetails_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
    )
  }

  @Test
  fun getAccountDetails_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
    )
  }

  @Test
  fun getAccountDetails_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
    )
  }

  @Test
  fun getAccountDetails_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
    )
  }
  // endregion getAccountDetails EDGE CASE
}