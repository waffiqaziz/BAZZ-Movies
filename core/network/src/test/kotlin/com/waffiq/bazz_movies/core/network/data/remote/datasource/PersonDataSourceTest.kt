package com.waffiq.bazz_movies.core.network.data.remote.datasource

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
import retrofit2.Response

class PersonDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getPersonDetails_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      mockApiResponse = Response.success(personResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
      expectedData = personResponseDump,
    )
  }

  @Test
  fun getPersonDetails_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonDetails EDGE CASE
  @Test
  fun getPersonDetails_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
    )
  }

  @Test
  fun getPersonDetails_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
    )
  }

  @Test
  fun getPersonDetails_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
    )
  }

  @Test
  fun getPersonDetails_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
    )
  }

  @Test
  fun getPersonDetails_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
    )
  }

  @Test
  fun getPersonDetails_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonDetails(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetails(1810) },
    )
  }
  // endregion getPersonDetails EDGE CASE

  @Test
  fun getPersonImages_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      mockApiResponse = Response.success(imagePersonResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
      expectedData = imagePersonResponseDump,
    )
  }

  @Test
  fun getPersonImages_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonImages EDGE CASE
  @Test
  fun getPersonImages_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
    )
  }

  @Test
  fun getPersonImages_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
    )
  }

  @Test
  fun getPersonImages_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
    )
  }

  @Test
  fun getPersonImages_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
    )
  }

  @Test
  fun getPersonImages_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
    )
  }

  @Test
  fun getPersonImages_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonImages(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImages(1878952) },
    )
  }
  // endregion getPersonImages EDGE CASE

  @Test
  fun getPersonCredits_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      mockApiResponse = Response.success(combinedCreditResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
      expectedData = combinedCreditResponseDump,
    )
  }

  @Test
  fun getPersonCredits_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonCredits EDGE CASE
  @Test
  fun getPersonCredits_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
    )
  }

  @Test
  fun getPersonCredits_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
    )
  }

  @Test
  fun getPersonCredits_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
    )
  }

  @Test
  fun getPersonCredits_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
    )
  }

  @Test
  fun getPersonCredits_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
    )
  }

  @Test
  fun getPersonCredits_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonCredits(500) },
    )
  }
  // endregion getPersonCredits EDGE CASE

  @Test
  fun getPersonExternalIds_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      mockApiResponse = Response.success(externalIDPersonResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
      expectedData = externalIDPersonResponseDump,
    )
  }

  @Test
  fun getPersonExternalIds_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonExternalIds EDGE CASE
  @Test
  fun getPersonExternalIds_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
    )
  }

  @Test
  fun getPersonExternalIds_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
    )
  }

  @Test
  fun getPersonExternalIds_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
    )
  }

  @Test
  fun getPersonExternalIds_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
    )
  }

  @Test
  fun getPersonExternalIds_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
    )
  }

  @Test
  fun getPersonExternalIds_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonExternalIds(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalIds(114253) },
    )
  }
  // endregion getPersonExternalIds EDGE CASE
}
