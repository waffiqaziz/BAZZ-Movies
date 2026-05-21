package com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.omdbDetailsResponseDump
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

class OMDbDetailDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getOMDbDetails_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        mockApiResponse = success(omdbDetailsResponseDump),
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
        expectedData = omdbDetailsResponseDump,
      )
    }

  @Test
  fun getOMDbDetails_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
        expectedErrorMessage = "The API is undergoing maintenance. Try again later.",
      )
    }

  // region getOMDbDetails EDGE CASE
  @Test
  fun getOMDbDetails_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }
  // endregion getOMDbDetails EDGE CASE
}
