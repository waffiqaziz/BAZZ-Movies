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

class AccountPostFavoriteRemoteDataSourceTest : BaseMediaDataSourceTest() {

  @Test
  fun postFavorite_whenSuccessful_returnsExpectedResponse() = runTest {
    testSuccessResponse(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      mockApiResponse = Response.success(postResponseSuccessDump),
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
      expectedData = postResponseSuccessDump,
    )
  }

  @Test
  fun postFavorite_whenErrorOccurs_returnsExpectedStatusMessageResponse() = runTest {
    testErrorResponse(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      errorResponse = invalidServiceErrorResponse,
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
      expectedErrorMessage = "Invalid service: this service does not exist."
    )
  }

  // region postFavorite EDGE CASE
  @Test
  fun postFavorite_whenAPIRespondsWith404_returnsExpectedResponse() = runTest {
    testError404Response(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_whenNetworkErrorOccurs_returnsExpectedResponse() = runTest {
    testUnknownHostExceptionResponse(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_whenTimeoutOccurs_returnsErrorResponse() = runTest {
    testSocketTimeoutExceptionResponse(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_whenHttpExceptionOccurs_returnsErrorResponse() = runTest {
    testHttpExceptionResponse(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_whenIOExceptionOccurs_returnsErrorResponse() = runTest {
    testIOExceptionResponse(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
    )
  }

  @Test
  fun postFavorite_whenExceptionOccurs_returnsErrorResponse() = runTest {
    testGeneralExceptionResponse(
      apiEndpoint = { mockAccountApiService.postFavoriteTMDB(userId, sessionId, fav) },
      dataSourceEndpointCall = { accountRemoteDataSource.postFavorite(sessionId, fav, userId) },
    )
  }
  // endregion postFavorite EDGE CASE
}
