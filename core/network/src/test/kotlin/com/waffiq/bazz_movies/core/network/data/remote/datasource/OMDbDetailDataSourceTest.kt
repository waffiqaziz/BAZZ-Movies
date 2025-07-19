package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.omdbDetailsResponseDump
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class OMDbDetailDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun getOMDbDetail_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      mockApiResponse = Response.success(omdbDetailsResponseDump),
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
      expectedData = omdbDetailsResponseDump,
    ) { data ->
      assertEquals("Avatar: The Way of Water", data.title)
      assertEquals("tt1630029", data.imdbID)
      assertEquals("2022", data.year)
      assertEquals("James Cameron, Rick Jaffa, Amanda Silver", data.writer)
    }
  }

  @Test
  fun getOMDbDetail_whenServerError_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getOMDbDetails EDGE CASE
  @Test
  fun getOMDbDetail_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
    )
  }

  @Test
  fun getOMDbDetail_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
    )
  }

  @Test
  fun getOMDbDetail_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
    )
  }

  @Test
  fun getOMDbDetail_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
    )
  }

  @Test
  fun getOMDbDetail_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
    )
  }

  @Test
  fun getOMDbDetail_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { omDbApiService.getOMDbDetails("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getOMDbDetails("tt1630029") },
    )
  }
  // endregion getOMDbDetails EDGE CASE
}
