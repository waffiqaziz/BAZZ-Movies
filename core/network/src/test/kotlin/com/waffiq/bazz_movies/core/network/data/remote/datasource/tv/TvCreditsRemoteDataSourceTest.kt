package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCreditsResponseDump2
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

class TvCreditsRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvCredits_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      mockApiResponse = Response.success(mediaCreditsResponseDump2),
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
      expectedData = mediaCreditsResponseDump2,
    )
  }

  @Test
  fun getTvCredits_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvCredits EDGE CASE
  @Test
  fun getTvCredits_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
    )
  }

  @Test
  fun getTvCredits_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvCredits(22222) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvCredits(22222) },
    )
  }
  // endregion getTvCredits EDGE CASE
}
