package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.statedResponseDump1
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

class MovieStateRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieState_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        mockApiResponse = Response.success(statedResponseDump1),
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
        expectedData = statedResponseDump1,
      )
    }

  @Test
  fun getMovieState_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
        expectedErrorMessage = apiMaintenanceErrorMessage,
      )
    }

  // region getMovieState EDGE CASE
  @Test
  fun getMovieState_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
      )
    }

  @Test
  fun getMovieState_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
      )
    }

  @Test
  fun getMovieState_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
      )
    }

  @Test
  fun getMovieState_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
      )
    }

  @Test
  fun getMovieState_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
      )
    }

  @Test
  fun getMovieState_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieState(12345, "session_id") },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieState("session_id", 12345) },
      )
    }
  // endregion getMovieState EDGE CASE
}
