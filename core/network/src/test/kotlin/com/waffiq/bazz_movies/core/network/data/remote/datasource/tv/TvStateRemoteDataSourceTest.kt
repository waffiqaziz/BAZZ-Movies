package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.statedResponseDump2
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

class TvStateRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvState_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      mockApiResponse = Response.success(statedResponseDump2),
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
      expectedData = statedResponseDump2,
    )
  }

  @Test
  fun getTvState_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvState EDGE CASE
  @Test
  fun getTvState_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
    )
  }

  @Test
  fun getTvState_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvState(544321, "session_id") },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvState("session_id", 544321) },
    )
  }
  // endregion getTvState EDGE CASE
}
