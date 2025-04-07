package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.OMDbApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailMovieResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailTvResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.externalIdResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCreditsResponseDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieTvCreditsResponseDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.omdbDetailsResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.statedResponseDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.statedResponseDump2
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.videoMovieResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.videoTvResponseDump
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class MovieDataSourceDetailTest {

  private val apiMaintenanceErrorResponse: Response<PostResponse> = Response.error(
    503,
    """{"status_code": 503, "status_message": "The API is undergoing maintenance. Try again later."}"""
      .toResponseBody("application/json".toMediaTypeOrNull())
  )

  @MockK
  private lateinit var tmdbApiService: TMDBApiService

  @MockK
  private lateinit var omDbApiService: OMDbApiService

  @MockK
  private lateinit var testDispatcher: Dispatchers

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private lateinit var movieDataSource: MovieDataSource

  @Before
  fun setup() {
    // Initialize MockK annotations and relax mocking behavior
    // `relaxed = true` allows MockK to automatically provide default behavior for any unmocked method calls.
    MockKAnnotations.init(this, relaxed = true)

    // clear any previous mocks to ensure tests are isolated
    clearMocks(tmdbApiService, omDbApiService)

    movieDataSource = MovieDataSource(tmdbApiService, omDbApiService, testDispatcher.IO)
  }

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
  fun getDetailOMDb_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      mockApiResponse = Response.success(omdbDetailsResponseDump),
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
      expectedData = omdbDetailsResponseDump,
    ) { data ->
      assertEquals("Avatar: The Way of Water", data.title)
      assertEquals("tt1630029", data.imdbID)
      assertEquals("2022", data.year)
      assertEquals("James Cameron, Rick Jaffa, Amanda Silver", data.writer)
    }
  }

  @Test
  fun getDetailOMDbError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getDetailOMDb EDGE CASE
  @Test
  fun getDetailOMDb_ReturnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_ReturnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }
  // endregion getDetailOMDb EDGE CASE

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
