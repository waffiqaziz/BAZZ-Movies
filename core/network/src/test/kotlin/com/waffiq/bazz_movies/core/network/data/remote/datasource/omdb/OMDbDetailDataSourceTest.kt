package com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class OMDbDetailDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getOMDbDetails_whenSuccessful_returnsExpectedResponse() =
    runTest {
      TestHelper.testSuccessResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        mockApiResponse = Response.success(DataDumpManager.omdbDetailsResponseDump),
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
        expectedData = DataDumpManager.omdbDetailsResponseDump,
      )
    }

  @Test
  fun getOMDbDetails_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      TestHelper.testErrorResponse(
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
      TestHelper.testError404Response(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      TestHelper.testUnknownHostExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testHttpExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testIOExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }

  @Test
  fun getOMDbDetails_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testGeneralExceptionResponse(
        apiEndpoint = { mockOmdbApiService.getOMDbDetails("tt1630029") },
        dataSourceEndpointCall = { omdbRemoteDataSource.getOMDbDetails("tt1630029") },
      )
    }
  // endregion getOMDbDetails EDGE CASE
}
