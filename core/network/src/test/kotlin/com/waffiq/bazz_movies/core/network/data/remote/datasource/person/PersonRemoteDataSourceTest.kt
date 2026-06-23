package com.waffiq.bazz_movies.core.network.data.remote.datasource.person

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailPersonResponse
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

class PersonRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getPersonDetails_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        mockApiResponse = success(detailPersonResponse),
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
        expectedData = detailPersonResponse,
      )
    }

  @Test
  fun getPersonDetails_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        errorResponse = backendErrorResponse,
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
        expectedErrorMessage = backendErrorMessage,
      )
    }

  // region getPersonDetails EDGE CASE
  @Test
  fun getPersonDetails_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
      )
    }

  @Test
  fun getPersonDetails_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
      )
    }

  @Test
  fun getPersonDetails_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
      )
    }

  @Test
  fun getPersonDetails_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
      )
    }

  @Test
  fun getPersonDetails_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
      )
    }

  @Test
  fun getPersonDetails_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonDetails(1810) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
      )
    }
  // endregion getPersonDetails EDGE CASE
}
