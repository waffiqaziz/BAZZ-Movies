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
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class PersonDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getDetailPerson_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      mockApiResponse = Response.success(personResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
      expectedData = personResponseDump,
    ) { data ->
      assertEquals("Heath Ledger", data.name)
      assertEquals(1810, data.id)
      assertEquals("nm0005132", data.imdbId)
      assertEquals("Acting", data.knownForDepartment)
    }
  }

  @Test
  fun getDetailPerson_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonDetail EDGE CASE
  @Test
  fun getPersonDetail_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
    )
  }

  @Test
  fun getDetailPerson_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
    )
  }

  @Test
  fun getDetailPerson_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
    )
  }

  @Test
  fun getDetailPerson_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
    )
  }

  @Test
  fun getDetailPerson_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
    )
  }

  @Test
  fun getPersonDetail_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getPersonDetail(1810) },
    )
  }
  // endregion getPersonDetail EDGE CASE

  @Test
  fun getImagePerson_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      mockApiResponse = Response.success(imagePersonResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
      expectedData = imagePersonResponseDump,
    ) { data ->
      assertEquals("/iJ1ekhu73bCRkpggLiKQh6MoHi8.jpg", data.profiles?.get(4)?.filePath)
      assertEquals(1878952, data.id)
      assertNull(data.profiles?.get(4)?.iso6391)
    }
  }

  @Test
  fun getImagePerson_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonImage EDGE CASE
  @Test
  fun getPersonImage_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
    )
  }

  @Test
  fun getImagePerson_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
    )
  }

  @Test
  fun getImagePerson_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
    )
  }

  @Test
  fun getImagePerson_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
    )
  }

  @Test
  fun getImagePerson_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
    )
  }

  @Test
  fun getPersonImage_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getPersonImage(1878952) },
    )
  }
  // endregion getPersonImage EDGE CASE

  @Test
  fun getPerson_KnownFor_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      mockApiResponse = Response.success(combinedCreditResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
      expectedData = combinedCreditResponseDump,
    ) { data ->
      assertEquals(500, data.id)
      assertEquals("War of the Worlds", data.cast?.get(0)?.title)
      assertEquals("Minority Report", data.cast?.get(1)?.title)
      assertEquals("The Last Samurai", data.crew?.get(0)?.title)
      assertEquals("Producer", data.crew?.get(0)?.job)
    }
  }

  @Test
  fun getPerson_KnownFor_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonKnownFor EDGE CASE
  @Test
  fun getPersonKnownFor_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
    )
  }

  @Test
  fun getPerson_KnownFor_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
    )
  }

  @Test
  fun getPerson_KnownFor_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
    )
  }

  @Test
  fun getPerson_KnownFor_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
    )
  }

  @Test
  fun getPerson_KnownFor_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
    )
  }

  @Test
  fun getPersonKnownFor_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getPersonCombinedCredits(500) },
      dataSourceEndpointCall = { movieDataSource.getPersonKnownFor(500) },
    )
  }
  // endregion getPersonKnownFor EDGE CASE

  @Test
  fun getExternalIDPerson_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      mockApiResponse = Response.success(externalIDPersonResponseDump),
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
      expectedData = externalIDPersonResponseDump,
    ) { data ->
      assertEquals(114253, data.id)
      assertEquals("/m/027xw9j", data.freebaseMid)
      assertEquals("nm1375030", data.imdbId)
      assertNull(data.freebaseId)
      assertNull(data.youtubeId)
      assertNull(data.instagramId)
    }
  }

  @Test
  fun getExternalIDPerson_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
      expectedErrorMessage = backendErrorMessage
    )
  }

  // region getPersonExternalID EDGE CASE
  @Test
  fun getPersonExternalID_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
    )
  }

  @Test
  fun getPersonExternalID_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getPersonExternalID(114253) },
    )
  }
  // endregion getPersonExternalID EDGE CASE
}
