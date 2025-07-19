package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
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
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class MovieDetailDataSourceTest : BaseMediaDataSourceTest() {

  private val expectedErrorMessageDump = "The API is undergoing maintenance. Try again later."

  @Test
  fun getCreditMovies_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      mockApiResponse = Response.success(mediaCreditsResponseDump1),
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
      expectedData = mediaCreditsResponseDump1,
    ) { data ->
      assertEquals("Alexa Goodall", data.cast[0].name)
      assertEquals("Martin Freeman", data.cast[1].name)
      assertEquals("Frank Schlegel", data.crew[0].name)
      assertEquals("Christian Ditter", data.crew[1].name)
    }
  }

  @Test
  fun getCreditMovies_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieCredits EDGE CASE
  @Test
  fun getCreditMovies_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieCredits(11111) },
      dataSourceEndpointCall = { movieDataSource.getMovieCredits(11111) },
    )
  }
  // endregion getMovieCredits EDGE CASE

  @Test
  fun getVideoMovies_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      mockApiResponse = Response.success(videoMovieResponseDump),
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
      expectedData = videoMovieResponseDump,
    ) { data ->
      assertEquals(
        "'Oppenheimer' Wins Best Cinematography | 96th Oscars (2024)",
        data.results[0].name
      )
      assertEquals(
        "'Oppenheimer' Wins Best Film Editing | 96th Oscars (2024)",
        data.results[1].name
      )
      assertEquals(
        "Cillian Murphy | Best Actor in a Leading Role | Oscars 2024 Press Room Speech",
        data.results[2].name
      )
    }
  }

  @Test
  fun getVideoMovies_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieVideo EDGE CASE
  @Test
  fun getVideoMovies_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieVideo(44444) },
      dataSourceEndpointCall = { movieDataSource.getMovieVideo(44444) },
    )
  }
  // endregion getMovieVideo EDGE CASE

  @Test
  fun getDetailMovie_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      mockApiResponse = Response.success(detailMovieResponseDump),
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
      expectedData = detailMovieResponseDump,
    ) { data ->
      assertEquals("Deadpool & Wolverine", data.title)
      assertEquals("tt6263850", data.imdbId)
      assertEquals("AD", data.releaseDatesResponse?.listReleaseDatesItemResponse?.get(0)?.iso31661)
      assertEquals(200000000, data.budget)
    }
  }

  @Test
  fun getDetailMovie_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieDetail EDGE CASE
  @Test
  fun getDetailMovie_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieDataSource.getMovieDetail(333333) },
    )
  }
  // endregion getMovieDetail EDGE CASE

  @Test
  fun getStatedMovie_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      mockApiResponse = Response.success(statedResponseDump1),
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
      expectedData = statedResponseDump1,
    ) { data ->
      assertEquals(12345, data.id)
      assertTrue(data.favorite)
      assertFalse(data.watchlist)
      assertEquals(10.0, (data.ratedResponse as RatedResponse.Value).value)
    }
  }

  @Test
  fun getStatedMovie_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getMovieState EDGE CASE
  @Test
  fun getStatedMovie_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getMovieState(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getMovieState("session_id", 12345) },
    )
  }
  // endregion getMovieState EDGE CASE
}
