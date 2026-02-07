package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailMovieResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCreditsResponseDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.statedResponseDump1
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

class MovieDetailDataSourceTest : BaseMediaDataSourceTest() {

  private val expectedErrorMessageDump = "The API is undergoing maintenance. Try again later."

  @Test
  fun getMovieCredits_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      mockApiResponse = Response.success(mediaCreditsResponseDump1),
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
      expectedData = mediaCreditsResponseDump1,
    )
  }

  @Test
  fun getMovieCredits_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieCredits EDGE CASE
  @Test
  fun getMovieCredits_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getMovieCredits_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getMovieCredits_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getMovieCredits_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getMovieCredits_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getMovieCredits_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }
  // endregion getMovieCredits EDGE CASE

  @Test
  fun getMovieVideo_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      mockApiResponse = Response.success(videoMovieResponseDump),
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
      expectedData = videoMovieResponseDump,
    )
  }

  @Test
  fun getMovieVideo_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieVideo EDGE CASE
  @Test
  fun getMovieVideo_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getMovieVideo_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }
  // endregion getMovieVideo EDGE CASE

  @Test
  fun getMovieDetail_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      mockApiResponse = Response.success(detailMovieResponseDump),
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
      expectedData = detailMovieResponseDump,
    )
  }

  @Test
  fun getMovieDetail_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieDetail EDGE CASE
  @Test
  fun getMovieDetail_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }
  // endregion getMovieDetail EDGE CASE

  @Test
  fun getMovieState_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      mockApiResponse = Response.success(statedResponseDump1),
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
      expectedData = statedResponseDump1,
    )
  }

  @Test
  fun getMovieState_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieState EDGE CASE
  @Test
  fun getMovieState_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getMovieState_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getMovieState_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getMovieState_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getMovieState_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getMovieState_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }
  // endregion getMovieState EDGE CASE
}
