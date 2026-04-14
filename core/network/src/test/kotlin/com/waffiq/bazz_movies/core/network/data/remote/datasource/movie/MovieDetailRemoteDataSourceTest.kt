package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class MovieDetailRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getMovieDetail_whenSuccessful_returnsExpectedResponse() = runTest {
    TestHelper.testSuccessResponse(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      mockApiResponse = Response.success(DataDumpManager.detailMovieResponseDump),
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      expectedData = DataDumpManager.detailMovieResponseDump,
    )
  }

  @Test
  fun getMovieDetail_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    TestHelper.testErrorResponse(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getMovieDetail EDGE CASE
  @Test
  fun getMovieDetail_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    TestHelper.testError404Response(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    TestHelper.testUnknownHostExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    TestHelper.testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testHttpExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testIOExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
    )
  }

  @Test
  fun getMovieDetail_whenExceptionOccurs_returnsErrorResponse() = runTest {
    TestHelper.testGeneralExceptionResponse(
      apiEndpoint = { mockMovieApiService.getMovieDetail(333333) },
      dataSourceEndpointCall = { movieRemoteDataSource.getMovieDetail(333333) },
    )
  }
  // endregion getMovieDetail EDGE CASE
}