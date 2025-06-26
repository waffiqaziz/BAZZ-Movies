package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailMovieResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCreditsResponseDump1
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

class MovieDataSourceDetailMovieTest : BaseMovieDataSourceTest() {

  private val expectedErrorMessageDump = "The API is undergoing maintenance. Try again later."

  @Test
  fun getCreditMovies_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      mockApiResponse = Response.success(movieTvCreditsResponseDump1),
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
      expectedData = movieTvCreditsResponseDump1,
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
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getCreditMovies EDGE CASE
  @Test
  fun getCreditMovies_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }
  // endregion getCreditMovies EDGE CASE

  @Test
  fun getVideoMovies_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      mockApiResponse = Response.success(videoMovieResponseDump),
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
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
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getVideoMovies EDGE CASE
  @Test
  fun getVideoMovies_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }
  // endregion getVideoMovies EDGE CASE

  @Test
  fun getDetailMovie_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      mockApiResponse = Response.success(detailMovieResponseDump),
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
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
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getDetailMovie EDGE CASE
  @Test
  fun getDetailMovie_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }
  // endregion getDetailMovie EDGE CASE

  @Test
  fun getStatedMovie_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      mockApiResponse = Response.success(statedResponseDump1),
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
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
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
      expectedErrorMessage = expectedErrorMessageDump
    )
  }

  // region getStatedMovie EDGE CASE
  @Test
  fun getStatedMovie_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }
  // endregion getStatedMovie EDGE CASE
}
