package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class MovieDataSourcePersonTest : BaseMovieDataSourceTest() {

  private val backendErrorResponse: Response<PostResponse> = Response.error(
    502,
    """{"status_code": 502, "status_message": "Couldn't connect to the backend server."}"""
      .toResponseBody("application/json".toMediaTypeOrNull())
  )

  @Test
  fun getDetailPerson_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      mockApiResponse = Response.success(personResponseDump),
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
      expectedData = personResponseDump,
    ) { data ->
      assertEquals("Heath Ledger", data.name)
      assertEquals(1810, data.id)
      assertEquals("nm0005132", data.imdbId)
      assertEquals("Acting", data.knownForDepartment)
    }
  }

  @Test
  fun getDetailPersonError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
      expectedErrorMessage = "Couldn't connect to the backend server."
    )
  }

  // region getDetailPerson EDGE CASE
  @Test
  fun getDetailPerson_ReturnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
    )
  }

  @Test
  fun getDetailPerson_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
    )
  }

  @Test
  fun getDetailPerson_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
    )
  }

  @Test
  fun getDetailPerson_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
    )
  }

  @Test
  fun getDetailPerson_ReturnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
    )
  }

  @Test
  fun getDetailPerson_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getDetailPerson(1810) },
      dataSourceEndpointCall = { movieDataSource.getDetailPerson(1810) },
    )
  }
  // endregion getDetailPerson EDGE CASE

  @Test
  fun getImagePerson_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      mockApiResponse = Response.success(imagePersonResponseDump),
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
      expectedData = imagePersonResponseDump,
    ) { data ->
      assertEquals("/iJ1ekhu73bCRkpggLiKQh6MoHi8.jpg", data.profiles?.get(4)?.filePath)
      assertEquals(1878952, data.id)
      assertNull(data.profiles?.get(4)?.iso6391)
    }
  }

  @Test
  fun getImagePersonError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
      expectedErrorMessage = "Couldn't connect to the backend server."
    )
  }

  // region getImagePerson EDGE CASE
  @Test
  fun getImagePerson_ReturnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
    )
  }

  @Test
  fun getImagePerson_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
    )
  }

  @Test
  fun getImagePerson_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
    )
  }

  @Test
  fun getImagePerson_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
    )
  }

  @Test
  fun getImagePerson_ReturnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
    )
  }

  @Test
  fun getImagePerson_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getImagePerson(1878952) },
      dataSourceEndpointCall = { movieDataSource.getImagePerson(1878952) },
    )
  }
  // endregion getImagePerson EDGE CASE

  @Test
  fun getKnownForPerson_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      mockApiResponse = Response.success(combinedCreditResponseDump),
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
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
  fun getKnownForPersonError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
      expectedErrorMessage = "Couldn't connect to the backend server."
    )
  }

  // region getKnownForPerson EDGE CASE
  @Test
  fun getKnownForPerson_ReturnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
    )
  }

  @Test
  fun getKnownForPerson_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
    )
  }

  @Test
  fun getKnownForPerson_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
    )
  }

  @Test
  fun getKnownForPerson_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
    )
  }

  @Test
  fun getKnownForPerson_ReturnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
    )
  }

  @Test
  fun getKnownForPerson_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getKnownForPersonCombinedMovieTv(500) },
      dataSourceEndpointCall = { movieDataSource.getKnownForPerson(500) },
    )
  }
  // endregion getKnownForPerson EDGE CASE

  @Test
  fun getExternalIDPerson_ReturnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      mockApiResponse = Response.success(externalIDPersonResponseDump),
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
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
  fun getExternalIDPersonError_ReturnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      errorResponse = backendErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
      expectedErrorMessage = "Couldn't connect to the backend server."
    )
  }

  // region getExternalIDPerson EDGE CASE
  @Test
  fun getExternalIDPerson_ReturnErrorWhenApiRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_ReturnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_ReturnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_ReturnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_ReturnErrorWhenIoExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
    )
  }

  @Test
  fun getExternalIDPerson_ReturnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getExternalIdPerson(114253) },
      dataSourceEndpointCall = { movieDataSource.getExternalIDPerson(114253) },
    )
  }
  // endregion getExternalIDPerson EDGE CASE
}
