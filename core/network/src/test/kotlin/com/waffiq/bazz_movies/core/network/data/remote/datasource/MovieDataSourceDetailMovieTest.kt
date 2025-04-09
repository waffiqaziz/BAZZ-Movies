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

  @Test
  fun getCreditMovies_ReturnExpectedResponse() = runTest {
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
  fun getCreditMoviesError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getCreditMovies EDGE CASE
  @Test
  fun getCreditMovies_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }

  @Test
  fun getCreditMovies_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getCreditMovies(11111) },
      dataSourceEndpointCall = { movieDataSource.getCreditMovies(11111) },
    )
  }
  // endregion getCreditMovies EDGE CASE

  @Test
  fun getVideoMovies_ReturnExpectedResponse() = runTest {
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
  fun getVideoMoviesError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getVideoMovies EDGE CASE
  @Test
  fun getVideoMovies_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }

  @Test
  fun getVideoMovies_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getVideoMovies(44444) },
      dataSourceEndpointCall = { movieDataSource.getVideoMovies(44444) },
    )
  }
  // endregion getVideoMovies EDGE CASE

  @Test
  fun getDetailMovie_ReturnExpectedResponse() = runTest {
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
  fun getDetailMovieError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getDetailMovie EDGE CASE
  @Test
  fun getDetailMovie_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }

  @Test
  fun getDetailMovie_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailMovie(333333) },
      dataSourceEndpointCall = { movieDataSource.getDetailMovie(333333) },
    )
  }
  // endregion getDetailMovie EDGE CASE

  @Test
  fun getStatedMovie_ReturnExpectedResponse() = runTest {
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
  fun getStatedMovieError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getStatedMovie EDGE CASE
  @Test
  fun getStatedMovie_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }

  @Test
  fun getStatedMovie_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getStatedMovie(12345, "session_id") },
      dataSourceEndpointCall = { movieDataSource.getStatedMovie("session_id", 12345) },
    )
  }
  // endregion getStatedMovie EDGE CASE
}
