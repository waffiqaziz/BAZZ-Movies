package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

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
import retrofit2.Response

class TvPostRateRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun postTvRateSuccess_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        mockApiResponse = Response.success(ratePostResponseSuccessDump),
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
        expectedData = ratePostResponseSuccessDump,
      )
    }

  @Test
  fun postTvRate_whenErrorOccurs_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        errorResponse = notAuthorizedErrorResponse,
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
        expectedErrorMessage = "Not Authorized",
      )
    }

  // region postTvRate EDGE CASE
  @Test
  fun postTvRate_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
      )
    }

  @Test
  fun postTvRate_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
      )
    }

  @Test
  fun postTvRate_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
      )
    }

  @Test
  fun postTvRate_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
      )
    }

  @Test
  fun postTvRate_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
      )
    }

  @Test
  fun postTvRate_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockTvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
        dataSourceEndpointCall = { tvRemoteDataSource.postTvRate(sessionId, rating, tvId) },
      )
    }
  // endregion postTvRate EDGE CASE
}
