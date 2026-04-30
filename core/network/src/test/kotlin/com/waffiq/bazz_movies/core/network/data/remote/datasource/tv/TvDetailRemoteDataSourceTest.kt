package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager
import com.waffiq.bazz_movies.core.network.testutils.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class TvDetailRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvDetail_whenSuccessful_returnsExpectedResponse() =
    runTest {
      TestHelper.testSuccessResponse(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        mockApiResponse = Response.success(DataDumpManager.detailTvResponseDump),
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
        expectedData = DataDumpManager.detailTvResponseDump,
      )
    }

  @Test
  fun getTvDetail_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      TestHelper.testErrorResponse(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
        expectedErrorMessage = apiMaintenanceErrorMessage,
      )
    }

  // region getTvDetail EDGE CASE
  @Test
  fun getTvDetail_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      TestHelper.testError404Response(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
      )
    }

  @Test
  fun getTvDetail_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      TestHelper.testUnknownHostExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
      )
    }

  @Test
  fun getTvDetail_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
      )
    }

  @Test
  fun getTvDetail_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testHttpExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
      )
    }

  @Test
  fun getTvDetail_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testIOExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
      )
    }

  @Test
  fun getTvDetail_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      TestHelper.testGeneralExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvDetail(253905) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvDetail(253905) },
      )
    }
  // endregion getTvDetail EDGE CASE
}
