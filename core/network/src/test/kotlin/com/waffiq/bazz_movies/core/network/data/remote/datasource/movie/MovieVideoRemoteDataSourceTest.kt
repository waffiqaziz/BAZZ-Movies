package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.videoMovieResponseDump
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

class MovieVideoRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieVideo_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      mockApiResponse = Response.success(videoMovieResponseDump),
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
      expectedData = videoMovieResponseDump,
    )
  }

  @Test
  fun getMovieVideo_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getMovieVideo EDGE CASE
  @Test
  fun getMovieVideo_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieVideo(44444) },
    )
  }
  // endregion getMovieVideo EDGE CASE
}
