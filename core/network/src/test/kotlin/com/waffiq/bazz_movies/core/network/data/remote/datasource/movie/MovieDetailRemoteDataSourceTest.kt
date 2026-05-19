package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailMovieResponseDump
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

class MovieDetailRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieDetail_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        mockApiResponse = success(detailMovieResponseDump),
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
        expectedData = detailMovieResponseDump,
      )
    }

  @Test
  fun getMovieDetail_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
        expectedErrorMessage = apiMaintenanceErrorMessage,
      )
    }

  // region getMovieDetail EDGE CASE
  @Test
  fun getMovieDetail_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      )
    }

  @Test
  fun getMovieDetail_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      )
    }

  @Test
  fun getMovieDetail_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      )
    }

  @Test
  fun getMovieDetail_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      )
    }

  @Test
  fun getMovieDetail_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      )
    }

  @Test
  fun getMovieDetail_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
        dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      )
    }
  // endregion getMovieDetail EDGE CASE
}
