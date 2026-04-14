package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.externalIdResponseDump
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

class TvExternalIdsRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvExternalIds_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      mockApiResponse = Response.success(externalIdResponseDump),
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
      expectedData = externalIdResponseDump,
    )
  }

  @Test
  fun getTvExternalIds_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
      expectedErrorMessage = apiMaintenanceErrorMessage
    )
  }

  // region getTvExternalIds EDGE CASE
  @Test
  fun getTvExternalIds_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
    )
  }

  @Test
  fun getTvExternalIds_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { mockTvApiService.getTvExternalIds(246) },
      dataSourceEndpointCall = { tvRemoteDataSource.getTvExternalIds(246) },
    )
  }
  // endregion getTvExternalIds EDGE CASE
}
