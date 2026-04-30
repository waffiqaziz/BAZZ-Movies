package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvKeywordsResponse
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

class TvKeywordsRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getTvKeywords_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        mockApiResponse = Response.success(tvKeywordsResponse),
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
        expectedData = tvKeywordsResponse,
      )
    }

  @Test
  fun getTvKeywords_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        errorResponse = apiMaintenanceErrorResponse,
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
        expectedErrorMessage = "The API is undergoing maintenance. Try again later.",
      )
    }

  @Test
  fun getTvKeywords_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
      )
    }

  @Test
  fun getTvKeywords_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
      )
    }

  @Test
  fun getTvKeywords_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
      )
    }

  @Test
  fun getTvKeywords_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
      )
    }

  @Test
  fun getTvKeywords_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
      )
    }

  @Test
  fun getTvKeywords_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockTvApiService.getTvKeywords("tvId") },
        dataSourceEndpointCall = { tvRemoteDataSource.getTvKeywords("tvId") },
      )
    }
}
