package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
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
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class TvDetailDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTv_Credits_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      mockApiResponse = Response.success(mediaCreditsResponseDump2),
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
      expectedData = mediaCreditsResponseDump2,
    ) { data ->
      assertEquals("Zach Tyler Eisen", data.cast[0].name)
      assertEquals("Mae Whitman", data.cast[1].name)
      assertEquals("Dao Le", data.crew[0].name)
      assertEquals("Heiko von Drengenberg", data.crew[1].name)
    }
  }

  @Test
  fun getTv_Credits_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
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
  fun getTv_Credits_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTv_Credits_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTv_Credits_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { movieDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTv_Credits_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
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
  fun getVideoTv_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      mockApiResponse = Response.success(videoTvResponseDump),
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
      expectedData = videoTvResponseDump,
    ) { data ->
      assertEquals("JUJUTSU KAISEN Opening | Kaikai Kitan by Eve", data.results[0].name)
      assertEquals("Official Trailer 3 [Subtitled]", data.results[1].name)
      assertEquals("Official Trailer [Subtitled]", data.results[2].name)
    }
  }

  @Test
  fun getVideoTv_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvVideo EDGE CASE
  @Test
  fun getVideoTv_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getVideoTv_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getVideoTv_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getVideoTv_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getVideoTv_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }

  @Test
  fun getVideoTv_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvVideo(95479) },
      dataSourceEndpointCall = { movieDataSource.getTvVideo(95479) },
    )
  }
  // endregion getTvVideo EDGE CASE

  @Test
  fun getDetailTv_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      mockApiResponse = Response.success(detailTvResponseDump),
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
      expectedData = detailTvResponseDump,
    ) { data ->
      assertEquals(253905, data.id)
      assertEquals("When the Phone Rings", data.name)
      assertEquals("MBC", data.networksResponse?.get(0)?.name)
      assertEquals("South Korea", data.productionCountriesResponse?.get(0)?.name)
      assertEquals(8.4, data.voteAverage)
    }
  }

  @Test
  fun getDetailTv_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
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
  fun getDetailTv_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getDetailTv_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getDetailTv_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvDetail(253905) },
      dataSourceEndpointCall = { movieDataSource.getTvDetail(253905) },
    )
  }

  @Test
  fun getDetailTv_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
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
  fun getExternalTvId_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      mockApiResponse = Response.success(externalIdResponseDump),
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
      expectedData = externalIdResponseDump,
    ) { data ->
      assertEquals(246, data.id)
      assertEquals(74852, data.tvdbId)
      assertEquals(2680, data.tvrageId)
      assertEquals("avatarthelastairbender", data.facebookId)
      assertEquals("avatarthelastairbender", data.instagramId)
    }
  }

  @Test
  fun getExternalTvId_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
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
  fun getExternalTvId_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getExternalTvId_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getExternalTvId_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { movieDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getExternalTvId_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
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
  fun getStatedTv_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      mockApiResponse = Response.success(statedResponseDump2),
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
      expectedData = statedResponseDump2,
    ) { data ->
      assertEquals(544321, data.id)
      assertTrue(data.watchlist)
      assertFalse(data.favorite)
      assertTrue(data.ratedResponse is RatedResponse.Unrated)
    }
  }

  @Test
  fun getStatedTv_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
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
  fun getStatedTv_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
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
