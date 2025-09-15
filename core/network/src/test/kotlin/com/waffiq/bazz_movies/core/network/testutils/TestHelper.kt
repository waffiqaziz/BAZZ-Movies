package com.waffiq.bazz_movies.core.network.testutils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadResult
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.TestDiffCallback
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.TestListCallback
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Utility object providing shared helpers for testing paged and flow-based data sources.
 *
 * Includes support for:
 * - Verifying success and error responses from `Flow<NetworkResult<T>>`
 * - Testing various exception scenarios (e.g., network, HTTP, IO)
 * - Paging source testing with mocked data
 * - Paging flow testing using `AsyncPagingDataDiffer`
 */
object TestHelper {

  /**
   * Differ used for testing [PagingData] of [MediaResponseItem].
   */
  val differ = AsyncPagingDataDiffer(
    diffCallback = TestDiffCallback<MediaResponseItem>(),
    updateCallback = TestListCallback(),
    workerDispatcher = Dispatchers.Main
  )


  /**
   * Differ used for testing [PagingData] of [MultiSearchResponseItem].
   */
  val differSearch = AsyncPagingDataDiffer(
    diffCallback = TestDiffCallback<MultiSearchResponseItem>(),
    updateCallback = TestListCallback(),
    workerDispatcher = Dispatchers.Main
  )

  /**
   * Verifies a successful flow response from a data source.
   *
   * @param apiEndpoint The suspended API call.
   * @param mockApiResponse The mocked successful API response.
   * @param dataSourceEndpointCall The data source method returning [Flow]<[NetworkResult]<T>>.
   * @param expectedData The expected successful data.
   * @param additionalAssertions Optional lambda to perform further assertions on the result.
   */
  suspend fun <T> testSuccessResponse(
    apiEndpoint: suspend () -> Response<*>,
    mockApiResponse: Response<T>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<T>>,
    expectedData: T,
    additionalAssertions: (T) -> Unit,
  ) {
    coEvery { apiEndpoint() } returns mockApiResponse

    // collect the flow emitted by the data source
    dataSourceEndpointCall().test {
      // verify that the first emitted value is Loading
      val firstItem = awaitItem()
      assertTrue(firstItem is NetworkResult.Loading)

      // verify the second emitted value is Success with expected data
      val secondItem = awaitItem()
      assertTrue(secondItem is NetworkResult.Success)
      val successResult = secondItem as NetworkResult.Success
      assertEquals(expectedData, successResult.data)

      additionalAssertions(successResult.data)

      cancelAndIgnoreRemainingEvents()
    }
    coVerify { apiEndpoint() }
  }


  /**
   * Verifies a flow response with an exception (e.g., network failure).
   */
  suspend fun testErrorResponse(
    apiEndpoint: suspend () -> Response<*>,
    exception: Throwable,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
    expectedErrorMessage: String,
  ) {
    coEvery { apiEndpoint() } throws exception

    dataSourceEndpointCall().test {
      val firstItem = awaitItem()
      assertTrue(firstItem is NetworkResult.Loading)

      val secondItem = awaitItem()
      assertTrue(secondItem is NetworkResult.Error)
      val errorResult = secondItem as NetworkResult.Error
      assertEquals(expectedErrorMessage, errorResult.message)

      cancelAndIgnoreRemainingEvents()
    }
    coVerify { apiEndpoint() }
  }

  /**
   * Verifies a flow response with an error HTTP response (e.g., 4xx/5xx).
   */
  suspend fun testErrorResponse(
    apiEndpoint: suspend () -> Response<*>,
    errorResponse: Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
    expectedErrorMessage: String,
  ) {
    coEvery { apiEndpoint() } returns errorResponse

    dataSourceEndpointCall().test {
      val firstItem = awaitItem()
      assertTrue(firstItem is NetworkResult.Loading)

      val secondItem = awaitItem()
      assertTrue(secondItem is NetworkResult.Error)
      val errorResult = secondItem as NetworkResult.Error
      assertEquals(expectedErrorMessage, errorResult.message)

      cancelAndIgnoreRemainingEvents()
    }
    coVerify { apiEndpoint() }
  }

  /**
   * Verifies a 404 Not Found error case.
   */
  suspend fun testError404Response(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
  ) {
    val error404Response: Response<MediaCreditsResponse> = Response.error(
      404,
      "Not Found".toResponseBody("application/json".toMediaTypeOrNull())
    )

    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      errorResponse = error404Response,
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      expectedErrorMessage = "Invalid request (400)"
    )
  }

  /**
   * Verifies an [UnknownHostException] error scenario.
   */

  suspend fun testUnknownHostExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = UnknownHostException("Unable to resolve host"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "Unable to resolve server hostname. Please check your internet connection."
    )
  }

  /**
   * Verifies a [SocketTimeoutException] error scenario.
   */
  suspend fun testSocketTimeoutExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = SocketTimeoutException("Timeout"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "Connection timed out. Please try again."
    )
  }


  /**
   * Verifies a generic [HttpException] error scenario.
   */
  suspend fun testHttpExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
  ) {
    val httpException = mockk<HttpException> {
      every { message } returns "HTTP Error"
    }
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = httpException,
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "Something went wrong"
    )
  }

  /**
   * Verifies an [IOException] scenario.
   */
  suspend fun testIOExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = IOException("Network error"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "Please check your network connection"
    )
  }

  /**
   * Verifies a generic [Exception] scenario.
   */
  suspend fun testGeneralExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = Exception("Unexpected error"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "An unknown error occurred"
    )
  }

  /**
   * Tests a paging source and asserts the loaded result.
   *
   * @param mockResults The mock API response.
   * @param mockApiCall The mocked API call.
   * @param loader The paging source's `load` method.
   * @param resultAssertions Assertions to verify [LoadResult.Page] content.
   */
  suspend fun testPagingSource(
    mockResults: MediaResponse,
    mockApiCall: suspend () -> MediaResponse,
    loader: suspend () -> LoadResult<Int, MediaResponseItem>,
    resultAssertions: (LoadResult.Page<Int, MediaResponseItem>) -> Unit,
  ) {
    coEvery { mockApiCall() } returns mockResults

    val result = loader()
    assert(result is LoadResult.Page)

    val page = result as LoadResult.Page
    resultAssertions(page)
    coVerify { mockApiCall() }
  }

  /**
   * Tests a paging source for search results.
   */
  suspend fun testPagingSearchSource(
    mockResults: MultiSearchResponse,
    mockApiCall: suspend () -> MultiSearchResponse,
    loader: suspend () -> LoadResult<Int, MultiSearchResponseItem>,
    resultAssertions: (LoadResult.Page<Int, MultiSearchResponseItem>) -> Unit,
  ) {
    coEvery { mockApiCall() } returns mockResults

    val result = loader()
    assert(result is LoadResult.Page)

    val page = result as LoadResult.Page
    resultAssertions(page)
    coVerify { mockApiCall() }
  }

  /**
   * Builds a default [MediaResponse] from a list of items.
   */
  fun defaultMediaResponse(list: List<MediaResponseItem>) = MediaResponse(
    page = 1,
    results = list,
    totalResults = 2,
    totalPages = 3
  )

  /**
   * Builds a default [MultiSearchResponse] from a list of items.
   */
  fun defaultMultiSearchResponse(list: List<MultiSearchResponseItem>) = MultiSearchResponse(
    page = 1,
    results = list,
    totalResults = 3,
    totalPages = 3
  )

  /**
   * Collects and verifies a [PagingData] flow for [MediaResponseItem].
   *
   * @param testScope The [TestScope] for advancing coroutine execution.
   * @param itemAssertions Assertions to verify the loaded items.
   */
  suspend fun Flow<PagingData<MediaResponseItem>>.testPagingFlow(
    testScope: TestScope,
    itemAssertions: (List<MediaResponseItem>) -> Unit,
  ) {
    test {
      val pagingData = awaitItem()
      val job = testScope.launch { differ.submitData(pagingData) }
      testScope.advanceUntilIdle()

      val pagingList = differ.snapshot().items
      itemAssertions(pagingList)
      assertTrue(pagingList.isNotEmpty())

      job.cancel()
      cancelAndIgnoreRemainingEvents()
    }
  }

  /**
   * Collects and verifies a [PagingData] flow for [MultiSearchResponseItem].
   */
  suspend fun Flow<PagingData<MultiSearchResponseItem>>.testPagingFlowSearch(
    testScope: TestScope,
    itemAssertions: (List<MultiSearchResponseItem>) -> Unit,
  ) {
    test {
      val pagingData = awaitItem()
      val job = testScope.launch { differSearch.submitData(pagingData) }
      testScope.advanceUntilIdle()

      val pagingList = differSearch.snapshot().items
      itemAssertions(pagingList)
      assertTrue(pagingList.isNotEmpty())

      job.cancel()
      cancelAndIgnoreRemainingEvents()
    }
  }
}
