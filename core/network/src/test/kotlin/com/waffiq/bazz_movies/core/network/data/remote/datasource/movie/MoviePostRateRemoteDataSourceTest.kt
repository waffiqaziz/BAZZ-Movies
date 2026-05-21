package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.data.remote.models.RatingRequest
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.ratePostResponseSuccessDump
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
import retrofit2.Response.success

class MoviePostRateRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun postMovieRateSuccess_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        mockApiResponse = success(ratePostResponseSuccessDump),
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
        expectedData = ratePostResponseSuccessDump,
      )
    }

  @Test
  fun postMovieRate_whenErrorOccurs_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        errorResponse = notAuthorizedErrorResponse,
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
        expectedErrorMessage = "Not Authorized",
      )
    }

  // region postMovieRate EDGE CASE
  @Test
  fun postMovieRate_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
      )
    }

  @Test
  fun postMovieRate_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
      )
    }

  @Test
  fun postMovieRate_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
      )
    }

  @Test
  fun postMovieRate_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
      )
    }

  @Test
  fun postMovieRate_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
      )
    }

  @Test
  fun postMovieRate_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating))
        },
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
      )
    }
  // endregion postMovieRate EDGE CASE
}
