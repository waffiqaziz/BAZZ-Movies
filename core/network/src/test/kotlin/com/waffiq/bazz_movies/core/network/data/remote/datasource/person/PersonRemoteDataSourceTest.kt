package com.waffiq.bazz_movies.core.network.data.remote.datasource.person

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.combinedCreditResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.externalIDPersonResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.imagePersonResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personResponseDump
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
        mockApiResponse = success(personResponseDump),
        dataSourceEndpointCall = { personRemoteDataSource.getPersonDetails(1810) },
        expectedData = personResponseDump,
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

  @Test
  fun getPersonCredits_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        mockApiResponse = success(combinedCreditResponseDump),
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
        expectedData = combinedCreditResponseDump,
      )
    }

  @Test
  fun getPersonCredits_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        errorResponse = backendErrorResponse,
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
        expectedErrorMessage = backendErrorMessage,
      )
    }

  // region getPersonCredits EDGE CASE
  @Test
  fun getPersonCredits_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
      )
    }

  @Test
  fun getPersonCredits_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
      )
    }

  @Test
  fun getPersonCredits_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
      )
    }

  @Test
  fun getPersonCredits_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
      )
    }

  @Test
  fun getPersonCredits_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
      )
    }

  @Test
  fun getPersonCredits_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonCredits(500) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonCredits(500) },
      )
    }
  // endregion getPersonCredits EDGE CASE

  @Test
  fun getPersonExternalIds_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        mockApiResponse = success(externalIDPersonResponseDump),
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
        expectedData = externalIDPersonResponseDump,
      )
    }

  @Test
  fun getPersonExternalIds_whenServerError_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        errorResponse = backendErrorResponse,
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
        expectedErrorMessage = backendErrorMessage,
      )
    }

  // region getPersonExternalIds EDGE CASE
  @Test
  fun getPersonExternalIds_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
      )
    }

  @Test
  fun getPersonExternalIds_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
      )
    }

  @Test
  fun getPersonExternalIds_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
      )
    }

  @Test
  fun getPersonExternalIds_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
      )
    }

  @Test
  fun getPersonExternalIds_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
      )
    }

  @Test
  fun getPersonExternalIds_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockPersonApiService.getPersonExternalIds(114253) },
        dataSourceEndpointCall = { personRemoteDataSource.getPersonExternalIds(114253) },
      )
    }
  // endregion getPersonExternalIds EDGE CASE
}
