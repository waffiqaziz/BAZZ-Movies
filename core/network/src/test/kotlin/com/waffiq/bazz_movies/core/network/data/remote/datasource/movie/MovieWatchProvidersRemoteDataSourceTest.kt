package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class MovieWatchProvidersRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieWatchProviders_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      mockApiResponse = Response.success(DataDumpManager.watchProviderResponseDump),
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      expectedData = DataDumpManager.watchProviderResponseDump,
    )
  }

  @Test
  fun getMovieWatchProviders_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getMovieWatchProviders EDGE CASE
  @Test
  fun getMovieWatchProviders_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
    )
  }

  @Test
  fun getMovieWatchProviders_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
    )
  }

  @Test
  fun getMovieWatchProviders_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
    )
  }

  @Test
  fun getMovieWatchProviders_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
    )
  }

  @Test
  fun getMovieWatchProviders_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
    )
  }

  @Test
  fun getMovieWatchProviders_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
    )
  }
  // endregion getMovieWatchProviders EDGE CASE
}
