package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.watchProviderResponseDump
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class MovieDataSourceWatchProvidersTest : BaseMovieDataSourceTest() {

  @Test
  fun getWatchProviders_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getWatchProviders("movie", 1234567890) },
      mockApiResponse = Response.success(watchProviderResponseDump),
      dataSourceEndpointCall = { movieDataSource.getWatchProviders("movie", 1234567890) },
      expectedData = watchProviderResponseDump,
    ) { data ->
      assertEquals(1234567890, data.id)

      val usProviders = data.results?.get("US")
      assertNotNull(usProviders)
      assertEquals("https://www.link.com/us/movie/example-movie", usProviders?.link)
      assertEquals(2, usProviders?.flatrate?.size)
      assertEquals("Netflix", usProviders?.flatrate?.get(0)?.providerName)
      assertEquals("Disney+", usProviders?.flatrate?.get(1)?.providerName)
      assertNotNull(usProviders?.rent?.get(0))

      val idProviders = data.results?.get("ID")
      assertNotNull(idProviders)
      assertEquals("https://www.link.com/id/movie/example-movie", idProviders?.link)
      assertEquals("Vidio", idProviders?.buy?.get(0)?.providerName)
      assertEquals("/logo6.png", idProviders?.free?.get(0)?.logoPath)
      assertEquals("WeTV", idProviders?.free?.get(0)?.providerName)
      assertEquals(6, idProviders?.free?.get(0)?.providerId)
      assertEquals(12, idProviders?.free?.get(0)?.displayPriority)
    }
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
