package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.watchProviderResponseDump
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
import retrofit2.Response

class WatchProvidersDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getWatchProviders_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      mockApiResponse = Response.success(watchProviderResponseDump),
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
      expectedData = watchProviderResponseDump,
    )
  }

  @Test
  fun getWatchProviders_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getWatchProviders EDGE CASE
  @Test
  fun getWatchProviders_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
    )
  }

  @Test
  fun getWatchProviders_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
    )
  }

  @Test
  fun getWatchProviders_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
    )
  }

  @Test
  fun getWatchProviders_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
    )
  }

  @Test
  fun getWatchProviders_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
    )
  }

  @Test
  fun getWatchProviders_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
    )
  }
  // endregion getWatchProviders EDGE CASE
}
