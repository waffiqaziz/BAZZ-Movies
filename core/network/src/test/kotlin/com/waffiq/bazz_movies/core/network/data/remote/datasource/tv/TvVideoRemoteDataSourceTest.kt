package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.videoTvResponseDump
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

class TvVideoRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvVideo_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        mockApiResponse = Response.success(videoTvResponseDump),
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
        expectedData = videoTvResponseDump,
      )
    }

  @Test
  fun getTvVideo_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
        expectedErrorMessage = apiMaintenanceErrorMessage,
      )
    }

  // region getTvVideo EDGE CASE
  @Test
  fun getTvVideo_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
      )
    }

  @Test
  fun getTvVideo_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
      )
    }

  @Test
  fun getTvVideo_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
      )
    }

  @Test
  fun getTvVideo_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
      )
    }

  @Test
  fun getTvVideo_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
      )
    }

  @Test
  fun getTvVideo_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvVideo(95479) },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvVideo(95479) },
      )
    }
  // endregion getTvVideo EDGE CASE
}
