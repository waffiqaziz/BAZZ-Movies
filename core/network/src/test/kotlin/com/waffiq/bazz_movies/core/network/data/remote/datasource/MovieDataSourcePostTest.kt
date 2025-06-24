package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.models.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.postResponseSuccessDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.ratePostResponseSuccessDump
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class MovieDataSourcePostTest : BaseMovieDataSourceTest() {

  private val userId = 123123
  private val sessionId = "session_id"
  private val tvId = 45678
  private val movieId = 121212
  private val rating = 9f
  private val fav = FavoritePostModel("tv", 35462654, false)
  private val wtc = WatchlistPostModel("movie", 345345, false)
  private val notAuthorizedErrorResponse: Response<PostResponse> = Response.error(
    401,
    """{"status_code": 401, "status_message": "Not Authorized"}"""
      .toResponseBody("application/json".toMediaTypeOrNull())
  )
  private val invalidServiceErrorResponse: Response<PostResponse> = Response.error(
    501,
    """{"status_code": 501, "status_message": "Invalid service: this service does not exist."}"""
      .toResponseBody("application/json".toMediaTypeOrNull())
  )
  private val invalidParameterErrorResponse: Response<PostResponse> = Response.error(
    422,
    """{"status_code": 422, "status_message": "Invalid parameters: Your request parameters are incorrect."}"""
      .toResponseBody("application/json".toMediaTypeOrNull())
  )

  @Test
  fun postFavorite_returnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      mockApiResponse = Response.success(postResponseSuccessDump),
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
      expectedData = postResponseSuccessDump,
    ) { data ->
      assertEquals(200, data.statusCode)
      assertEquals("Success", data.statusMessage)
    }
  }

  @Test
  fun postFavorite_errorShouldreturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      errorResponse = invalidServiceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
      expectedErrorMessage = "Invalid service: this service does not exist."
    )
  }

  // region postFavorite EDGE CASE
  @Test
  fun postFavorite_returnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_returnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { movieDataSource.postFavorite(sessionId, fav, userId) },
    )
  }
  // endregion postFavorite EDGE CASE

  @Test
  fun postWatchlistSuccess_returnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      mockApiResponse = Response.success(postResponseSuccessDump),
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
      expectedData = postResponseSuccessDump,
    ) { data ->
      assertEquals(200, data.statusCode)
      assertEquals("Success", data.statusMessage)
    }
  }

  @Test
  fun postWatchlist_errorShouldreturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      errorResponse = invalidParameterErrorResponse,
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
      expectedErrorMessage = "Invalid parameters: Your request parameters are incorrect."
    )
  }

  // region postWatchlist EDGE CASE
  @Test
  fun postWatchlist_returnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
    )
  }

  @Test
  fun postWatchlist_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
    )
  }

  @Test
  fun postWatchlist_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
    )
  }

  @Test
  fun postWatchlist_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
    )
  }

  @Test
  fun postWatchlist_returnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
    )
  }

  @Test
  fun postWatchlist_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
      dataSourceEndpointCall = { movieDataSource.postWatchlist(sessionId, wtc, userId) },
    )
  }
  // endregion postWatchlist EDGE CASE

  @Test
  fun postTvRateSuccess_returnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      mockApiResponse = Response.success(ratePostResponseSuccessDump),
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
      expectedData = ratePostResponseSuccessDump,
    ) { data ->
      assertEquals(200, data.statusCode)
      assertEquals("Successfully", data.statusMessage)
    }
  }

  @Test
  fun postTvRate_errorShouldreturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      errorResponse = notAuthorizedErrorResponse,
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
      expectedErrorMessage = "Not Authorized"
    )
  }

  // region postTvRate EDGE CASE
  @Test
  fun postTvRate_returnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
    )
  }

  @Test
  fun postTvRate_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
    )
  }

  @Test
  fun postTvRate_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
    )
  }

  @Test
  fun postTvRate_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
    )
  }

  @Test
  fun postTvRate_returnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
    )
  }

  @Test
  fun postTvRate_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postTvRate(sessionId, rating, tvId) },
    )
  }
  // endregion postTvRate EDGE CASE

  @Test
  fun postMovieRateSuccess_returnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      mockApiResponse = Response.success(ratePostResponseSuccessDump),
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
      expectedData = ratePostResponseSuccessDump,
    ) { data ->
      assertEquals(200, data.statusCode)
      assertEquals("Successfully", data.statusMessage)
    }
  }

  @Test
  fun postMovieRate_errorShouldreturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      errorResponse = notAuthorizedErrorResponse,
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
      expectedErrorMessage = "Not Authorized"
    )
  }

  // region postMovieRate EDGE CASE
  @Test
  fun postMovieRate_returnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
    )
  }

  @Test
  fun postMovieRate_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
    )
  }

  @Test
  fun postMovieRate_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
    )
  }

  @Test
  fun postMovieRate_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
    )
  }

  @Test
  fun postMovieRate_returnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
    )
  }

  @Test
  fun postMovieRate_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
      dataSourceEndpointCall = { movieDataSource.postMovieRate(sessionId, rating, movieId) },
    )
  }
  // endregion postMovieRate EDGE CASE
}
