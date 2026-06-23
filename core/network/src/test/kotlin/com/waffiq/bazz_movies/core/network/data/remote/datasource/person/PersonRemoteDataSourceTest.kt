package com.waffiq.bazz_movies.core.network.data.remote.datasource.person

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailPersonResponse
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.imagePersonResponseDump
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

  @Test
  fun getPersonImages_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        mockApiResponse = success(imagePersonResponseDump),
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
        expectedData = imagePersonResponseDump,
      )
    }

  @Test
  fun getPersonImages_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        errorResponse = backendErrorResponse,
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
        expectedErrorMessage = backendErrorMessage,
      )
    }

  // region getPersonImages EDGE CASE
  @Test
  fun getPersonImages_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
      )
    }

  @Test
  fun getPersonImages_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
      )
    }

  @Test
  fun getPersonImages_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
      )
    }

  @Test
  fun getPersonImages_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
      )
    }

  @Test
  fun getPersonImages_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
      )
    }

  @Test
  fun getPersonImages_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonImages(1878952) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonImages(1878952) },
      )
    }
  // endregion getPersonImages EDGE CASE
}
