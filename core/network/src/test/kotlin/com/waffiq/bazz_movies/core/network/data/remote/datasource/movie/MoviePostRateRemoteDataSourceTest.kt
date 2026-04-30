package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.data.remote.models.RatingRequest
import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class MoviePostRateRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun postMovieRateSuccess_whenSuccessful_returnsExpectedResponse() =
    runTest {
      TestHelper.testSuccessResponse(
        apiEndpoint = {
          mockMovieApiService.postMovieRate(
            movieId,
            sessionId,
            RatingRequest(rating),
          )
        },
        mockApiResponse = Response.success(DataDumpManager.ratePostResponseSuccessDump),
        dataSourceEndpointCall = {
          movieRemoteDataSource.postMovieRate(sessionId, rating, movieId)
        },
        expectedData = DataDumpManager.ratePostResponseSuccessDump,
      )
    }

  @Test
  fun postMovieRate_whenErrorOccurs_returnsExpectedStatusMessageResponse() =
    runTest {
      TestHelper.testErrorResponse(
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
      TestHelper.testError404Response(
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
      TestHelper.testUnknownHostExceptionResponse(
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
      TestHelper.testSocketTimeoutExceptionResponse(
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
      TestHelper.testHttpExceptionResponse(
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
      TestHelper.testIOExceptionResponse(
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
      TestHelper.testGeneralExceptionResponse(
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
