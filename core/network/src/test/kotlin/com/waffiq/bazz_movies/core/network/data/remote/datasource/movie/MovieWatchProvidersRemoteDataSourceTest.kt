package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

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
import retrofit2.Response.success

class MovieWatchProvidersRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieWatchProviders_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        mockApiResponse = success(watchProviderResponseDump),
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
        expectedData = watchProviderResponseDump,
      )
    }

  @Test
  fun getMovieWatchProviders_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
        expectedErrorMessage = "The API is undergoing maintenance. Try again later.",
      )
    }

  // region getMovieWatchProviders EDGE CASE
  @Test
  fun getMovieWatchProviders_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      )
    }

  @Test
  fun getMovieWatchProviders_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      )
    }

  @Test
  fun getMovieWatchProviders_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      )
    }

  @Test
  fun getMovieWatchProviders_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      )
    }

  @Test
  fun getMovieWatchProviders_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      )
    }

  @Test
  fun getMovieWatchProviders_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieWatchProviders(1234567890) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieWatchProviders(1234567890) },
      )
    }
  // endregion getMovieWatchProviders EDGE CASE
}
