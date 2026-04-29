package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class TvWatchProvidersRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvWatchProviders_whenSuccessful_returnsExpectedResponse() =
    runTest {
      TestHelper.testSuccessResponse(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        mockApiResponse = Response.success(DataDumpManager.watchProviderResponseDump),
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
        expectedData = DataDumpManager.watchProviderResponseDump,
      )
    }

  @Test
  fun getTvWatchProviders_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      TestHelper.testErrorResponse(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
        expectedErrorMessage = "The API is undergoing maintenance. Try again later.",
      )
    }

  // region getTvWatchProviders EDGE CASE
  @Test
  fun getTvWatchProviders_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      TestHelper.testError404Response(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
      )
    }

  @Test
  fun getTvWatchProviders_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      TestHelper.testUnknownHostExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
      )
    }

  @Test
  fun getTvWatchProviders_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
      )
    }

  @Test
  fun getTvWatchProviders_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testHttpExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
      )
    }

  @Test
  fun getTvWatchProviders_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testIOExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
      )
    }

  @Test
  fun getTvWatchProviders_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testGeneralExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvWatchProviders(1234567890) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvWatchProviders(1234567890) },
      )
    }
  // endregion getTvWatchProviders EDGE CASE
}
