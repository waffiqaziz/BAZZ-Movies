package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCreditsResponseDump1
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

class MovieCreditsRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieCredits_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        mockApiResponse = Response.success(mediaCreditsResponseDump1),
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
        expectedData = mediaCreditsResponseDump1,
      )
    }

  @Test
  fun getMovieCredits_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
        expectedErrorMessage = apiMaintenanceErrorMessage,
      )
    }

  // region getMovieCredits EDGE CASE
  @Test
  fun getMovieCredits_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
      )
    }

  @Test
  fun getMovieCredits_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
      )
    }

  @Test
  fun getMovieCredits_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
      )
    }

  @Test
  fun getMovieCredits_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
      )
    }

  @Test
  fun getMovieCredits_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
      )
    }

  @Test
  fun getMovieCredits_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieCredits(11111) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieCredits(11111) },
      )
    }
  // endregion getMovieCredits EDGE CASE
}
