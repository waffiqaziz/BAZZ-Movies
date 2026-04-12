package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieKeywordsResponse
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

class MovieKeywordsRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieKeywords_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      mockApiResponse = Response.success(movieKeywordsResponse),
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
      expectedData = movieKeywordsResponse,
    )
  }

  @Test
  fun getMovieKeywords_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  @Test
  fun getMovieKeywords_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
    )
  }

  @Test
  fun getMovieKeywords_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieKeywords("movieId") },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieKeywords("movieId") },
    )
  }
}
