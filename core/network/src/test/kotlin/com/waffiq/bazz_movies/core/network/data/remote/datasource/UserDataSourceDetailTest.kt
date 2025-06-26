package com.waffiq.bazz_movies.core.network.data.remote.datasource

import com.waffiq.bazz_movies.core.network.testutils.BaseUserDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.accountDetailsResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testError404Response
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testErrorResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testGeneralExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testHttpExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testIOExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSocketTimeoutExceptionResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testSuccessResponse
import com.waffiq.bazz_movies.core.network.testutils.TestHelper.testUnknownHostExceptionResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class UserDataSourceDetailTest : BaseUserDataSourceTest() {

  @Test
  fun getUserDetail_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      mockApiResponse = Response.success(accountDetailsResponse),
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
      expectedData = accountDetailsResponse,
    ) { data ->
      assertEquals(543798538, data.id)
      assertEquals("en", data.iso6391)
      assertEquals("USERNAME", data.username)
      assertTrue(data.includeAdult == false)
    }
  }

  @Test
  fun getUserDetail_whenSuccessful_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      errorResponse = apiInvalidFormatErrorResponse,
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
      expectedErrorMessage = "Invalid format: This service doesn't exist in that format."
    )
  }

  // region getUserDetail EDGE CASE
  @Test
  fun getUserDetail_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
    )
  }

  @Test
  fun getUserDetail_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
    )
  }

  @Test
  fun getUserDetail_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
    )
  }

  @Test
  fun getUserDetail_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
    )
  }

  @Test
  fun getUserDetail_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
    )
  }

  @Test
  fun getUserDetail_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { tmdbApiService.getAccountDetails("session_id") },
      dataSourceEndpointCall = { userDataSource.getUserDetail("session_id") },
    )
  }
  // endregion getUserDetail EDGE CASE
}
