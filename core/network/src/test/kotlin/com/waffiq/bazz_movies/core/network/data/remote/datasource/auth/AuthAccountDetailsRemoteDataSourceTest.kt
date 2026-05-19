package com.waffiq.bazz_movies.core.network.data.remote.datasource.auth

import com.waffiq.bazz_movies.core.network.testutils.BaseAuthDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.accountDetailsResponse
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

class AuthAccountDetailsRemoteDataSourceTest : BaseAuthDataSourceTest() {

  @Test
  fun getAccountDetails_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        mockApiResponse = success(accountDetailsResponse),
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
        expectedData = accountDetailsResponse,
      )
    }

  @Test
  fun getAccountDetails_whenSuccessful_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        errorResponse = apiInvalidFormatErrorResponse,
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
        expectedErrorMessage = "Invalid format: This service doesn't exist in that format.",
      )
    }

  // region getAccountDetails EDGE CASE
  @Test
  fun getAccountDetails_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      )
    }

  @Test
  fun getAccountDetails_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      )
    }

  @Test
  fun getAccountDetails_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      )
    }

  @Test
  fun getAccountDetails_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      )
    }

  @Test
  fun getAccountDetails_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      )
    }

  @Test
  fun getAccountDetails_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockAuthApiService.getAccountDetails("session_id") },
        dataSourceEndpointCall = { authRemoteDataSource.getAccountDetails("session_id") },
      )
    }
  // endregion getAccountDetails EDGE CASE
}
