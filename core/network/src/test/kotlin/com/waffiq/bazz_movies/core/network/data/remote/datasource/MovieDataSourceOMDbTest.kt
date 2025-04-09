package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseMovieDataSourceTest
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

class MovieDataSourceOMDbTest : BaseMovieDataSourceTest() {

  @Test
  fun getDetailOMDb_returnExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      mockApiResponse = Response.success(omdbDetailsResponseDump),
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
      expectedData = omdbDetailsResponseDump,
    ) { data ->
      assertEquals("Avatar: The Way of Water", data.title)
      assertEquals("tt1630029", data.imdbID)
      assertEquals("2022", data.year)
      assertEquals("James Cameron, Rick Jaffa, Amanda Silver", data.writer)
    }
  }

  @Test
  fun getDetailOMDbError_returnExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      errorResponse = apiMaintenanceErrorResponse,
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
      expectedErrorMessage = "The API is undergoing maintenance. Try again later."
    )
  }

  // region getDetailOMDb EDGE CASE
  @Test
  fun getDetailOMDb_returnErrorWhenAPIRespondsWith404() = runTest {
    testError404Response(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_returnErrorWhenNetworkErrorOccurs() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_returnErrorWhenTimeoutOccurs() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_returnErrorWhenHttpExceptionOccurs() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_returnErrorWhenIOExceptionOccurs() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }

  @Test
  fun getDetailOMDb_returnErrorWhenExceptionOccurs() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { omDbApiService.getMovieDetailOMDb("tt1630029") },
      dataSourceEndpointCall = { movieDataSource.getDetailOMDb("tt1630029") },
    )
  }
  // endregion getDetailOMDb EDGE CASE
}
