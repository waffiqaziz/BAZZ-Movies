package com.waffiq.bazz_movies.core.network.data.remote.datasource.account

import com.waffiq.bazz_movies.core.network.testutils.BaseMediaDataSourceTest
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.postResponseSuccessDump
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

class AccountPostWatchlistRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun postWatchlistSuccess_whenSuccessful_returnsExpectedResponse() =
    runTest {
      testSuccessResponse(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        mockApiResponse = Response.success(postResponseSuccessDump),
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
        expectedData = postResponseSuccessDump,
      )
    }

  @Test
  fun postWatchlist_whenErrorOccurs_returnsExpectedStatusMessageResponse() =
    runTest {
      testErrorResponse(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        errorResponse = invalidParameterErrorResponse,
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
        expectedErrorMessage = "Invalid parameters: Your request parameters are incorrect.",
      )
    }

  // region postWatchlist EDGE CASE
  @Test
  fun postWatchlist_whenAPIRespondsWith404_returnsExpectedResponse() =
    runTest {
      testError404Response(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
      )
    }

  @Test
  fun postWatchlist_whenNetworkErrorOccurs_returnsExpectedResponse() =
    runTest {
      testUnknownHostExceptionResponse(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
      )
    }

  @Test
  fun postWatchlist_whenTimeoutOccurs_returnsErrorResponse() =
    runTest {
      testSocketTimeoutExceptionResponse(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
      )
    }

  @Test
  fun postWatchlist_whenHttpExceptionOccurs_returnsErrorResponse() =
    runTest {
      testHttpExceptionResponse(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
      )
    }

  @Test
  fun postWatchlist_whenIOExceptionOccurs_returnsErrorResponse() =
    runTest {
      testIOExceptionResponse(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
      )
    }

  @Test
  fun postWatchlist_whenExceptionOccurs_returnsErrorResponse() =
    runTest {
      testGeneralExceptionResponse(
        apiEndpoint = { mockAccountApiService.postWatchlistTMDB(userId, sessionId, wtc) },
        dataSourceEndpointCall = { accountRemoteDataSource.postWatchlist(sessionId, wtc, userId) },
      )
    }
  // endregion postWatchlist EDGE CASE
}
