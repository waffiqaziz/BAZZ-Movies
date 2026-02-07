package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailTvResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.externalIdResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCreditsResponseDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.statedResponseDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.videoTvResponseDump
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

class TvDetailDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvCredits_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      mockApiResponse = Response.success(mediaCreditsResponseDump2),
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
      expectedData = mediaCreditsResponseDump2,
    )
  }

  @Test
  fun getTvCredits_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvCredits EDGE CASE
  @Test
  fun getTvCredits_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }
  // endregion getTvCredits EDGE CASE

  @Test
  fun getTvVideo_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      mockApiResponse = Response.success(videoTvResponseDump),
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
      expectedData = videoTvResponseDump,
    )
  }

  @Test
  fun getTvVideo_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvVideo EDGE CASE
  @Test
  fun getTvVideo_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getTvVideo_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getTvVideo_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getTvVideo_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getTvVideo_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getTvVideo_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }
  // endregion getTvVideo EDGE CASE

  @Test
  fun getTvDetail_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      mockApiResponse = Response.success(detailTvResponseDump),
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
      expectedData = detailTvResponseDump,
    )
  }

  @Test
  fun getTvDetail_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvDetail EDGE CASE
  @Test
  fun getTvDetail_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getTvDetail_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getTvDetail_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getTvDetail_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getTvDetail_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getTvDetail_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }
  // endregion getTvDetail EDGE CASE

  @Test
  fun getTvExternalIds_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      mockApiResponse = Response.success(externalIdResponseDump),
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
      expectedData = externalIdResponseDump,
    )
  }

  @Test
  fun getTvExternalIds_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvExternalIds EDGE CASE
  @Test
  fun getTvExternalIds_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }
  // endregion getTvExternalIds EDGE CASE

  @Test
  fun getTvState_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      mockApiResponse = Response.success(statedResponseDump2),
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
      expectedData = statedResponseDump2,
    )
  }

  @Test
  fun getTvState_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvState EDGE CASE
  @Test
  fun getTvState_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }
  // endregion getTvState EDGE CASE
}
