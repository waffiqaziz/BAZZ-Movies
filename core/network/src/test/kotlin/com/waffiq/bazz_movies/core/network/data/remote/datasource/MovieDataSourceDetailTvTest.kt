package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailTvResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.externalIdResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCreditsResponseDump2
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

class MovieDataSourceDetailTvTest : BaseMovieDataSourceTest() {

  @Test
  fun getCreditTv_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      mockApiResponse = Response.success(movieTvCreditsResponseDump2),
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
      expectedData = movieTvCreditsResponseDump2,
    ) { data ->
      assertEquals("Zach Tyler Eisen", data.cast[0].name)
      assertEquals("Mae Whitman", data.cast[1].name)
      assertEquals("Dao Le", data.crew[0].name)
      assertEquals("Heiko von Drengenberg", data.crew[1].name)
    }
  }

  @Test
  fun getCreditTvError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getCreditTv EDGE CASE
  @Test
  fun getCreditTv_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
    )
  }

  @Test
  fun getCreditTv_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
    )
  }

  @Test
  fun getCreditTv_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
    )
  }

  @Test
  fun getCreditTv_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
    )
  }

  @Test
  fun getCreditTv_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
    )
  }

  @Test
  fun getCreditTv_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditTv(22222) },
      dataSourceEndpointCall = { movieDataSource.getCreditTv(22222) },
    )
  }
  // endregion getCreditTv EDGE CASE

  @Test
  fun getVideoTv_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      mockApiResponse = Response.success(videoTvResponseDump),
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
      expectedData = videoTvResponseDump,
    ) { data ->
      assertEquals("JUJUTSU KAISEN Opening | Kaikai Kitan by Eve", data.results[0].name)
      assertEquals("Official Trailer 3 [Subtitled]", data.results[1].name)
      assertEquals("Official Trailer [Subtitled]", data.results[2].name)
    }
  }

  @Test
  fun getVideoTvError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getVideoTv EDGE CASE
  @Test
  fun getVideoTv_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
    )
  }

  @Test
  fun getVideoTv_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
    )
  }

  @Test
  fun getVideoTv_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
    )
  }

  @Test
  fun getVideoTv_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
    )
  }

  @Test
  fun getVideoTv_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
    )
  }

  @Test
  fun getVideoTv_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoTv(95479) },
      dataSourceEndpointCall = { movieDataSource.getVideoTv(95479) },
    )
  }
  // endregion getVideoTv EDGE CASE

  @Test
  fun getDetailTv_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      mockApiResponse = Response.success(detailTvResponseDump),
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
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
  fun getDetailTvError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getDetailTv EDGE CASE
  @Test
  fun getDetailTv_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
    )
  }

  @Test
  fun getDetailTv_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
    )
  }

  @Test
  fun getDetailTv_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
    )
  }

  @Test
  fun getDetailTv_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
    )
  }

  @Test
  fun getDetailTv_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
    )
  }

  @Test
  fun getDetailTv_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailTv(253905) },
      dataSourceEndpointCall = { movieDataSource.getDetailTv(253905) },
    )
  }
  // endregion getDetailTv EDGE CASE

  @Test
  fun getExternalTvId_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      mockApiResponse = Response.success(externalIdResponseDump),
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
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
  fun getExternalTvIdError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getExternalTvId EDGE CASE
  @Test
  fun getExternalTvId_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
    )
  }

  @Test
  fun getExternalTvId_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
    )
  }

  @Test
  fun getExternalTvId_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
    )
  }

  @Test
  fun getExternalTvId_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
    )
  }

  @Test
  fun getExternalTvId_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
    )
  }

  @Test
  fun getExternalTvId_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalId(246) },
      dataSourceEndpointCall = { movieDataSource.getExternalTvId(246) },
    )
  }
  // endregion getExternalTvId EDGE CASE

  @Test
  fun getStatedTv_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      mockApiResponse = Response.success(statedResponseDump2),
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
      expectedData = statedResponseDump2,
    ) { data ->
      assertEquals(544321, data.id)
      assertTrue(data.watchlist)
      assertFalse(data.favorite)
      assertTrue(data.ratedResponse is RatedResponse.Unrated)
    }
  }

  @Test
  fun getStatedTvError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getStatedTv EDGE CASE
  @Test
  fun getStatedTv_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
    )
  }

  @Test
  fun getStatedTv_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedTv(544321, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedTv("session_id", 544321) },
    )
  }
  // endregion getStatedTv EDGE CASE
}
