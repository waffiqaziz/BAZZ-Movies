package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieKeywordsResponse
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvKeywordsResponse
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

class MediaKeywordsDataSourceTest: BaseMediaDataSourceTest() {

  @Test
  fun getMovieKeywords_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      mockApiResponse = Response.success(movieKeywordsResponse),
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
      expectedData = movieKeywordsResponse,
    )
  }

  @Test
  fun getMovieKeywords_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getMovieKeywords EDGE CASE
  @Test
  fun getMovieKeywords_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieDataSource.getMovieKeywords("movieId") },
    )
  }
  // endregion getMovieKeywords EDGE CASE

  @Test
  fun getTvKeywords_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      mockApiResponse = Response.success(tvKeywordsResponse),
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
      expectedData = tvKeywordsResponse,
    )
  }

  @Test
  fun getTvKeywords_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getTvKeywords EDGE CASE
  @Test
  fun getTvKeywords_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
    )
  }

  @Test
  fun getTvKeywords_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
    )
  }

  @Test
  fun getTvKeywords_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
    )
  }

  @Test
  fun getTvKeywords_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
    )
  }

  @Test
  fun getTvKeywords_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
    )
  }

  @Test
  fun getTvKeywords_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvKeywords("tvId") },
      dataSourceEndpointCall = { movieDataSource.getTvKeywords("tvId") },
    )
  }
  // endregion getTvKeywords EDGE CASE
}
