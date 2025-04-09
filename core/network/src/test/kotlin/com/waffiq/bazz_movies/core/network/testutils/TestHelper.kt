package com.waffiq.bazz_movies.core.network.testutils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadResult
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MovieTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.castcrew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
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

object TestHelper {

  val differ = AsyncPagingDataDiffer(
    diffCallback = TestDiffCallback<ResultItemResponse>(),
    updateCallback = TestListCallback(),
    workerDispatcher = Dispatchers.Main
  )

  val differSearch = AsyncPagingDataDiffer(
    diffCallback = TestDiffCallback<ResultsItemSearchResponse>(),
    updateCallback = TestListCallback(),
    workerDispatcher = Dispatchers.Main
  )

  suspend fun <T> testSuccessResponse(
    apiEndpoint: suspend () -> Response<*>,
    mockApiResponse: Response<T>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<T>>,
    expectedData: T,
    additionalAssertions: (T) -> Unit
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

  suspend fun testErrorResponse(
    apiEndpoint: suspend () -> Response<*>,
    exception: Throwable,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
    expectedErrorMessage: String
  ) {
    coEvery { apiEndpoint() } throws exception

    dataSourceEndpointCall().test {
      val firstItem = awaitItem()
      assertTrue(firstItem is NetworkResult.Loading)

      var secondItem = awaitItem()
      assertTrue(secondItem is NetworkResult.Error)
      val errorResult = secondItem as NetworkResult.Error
      assertEquals(expectedErrorMessage, errorResult.message)

      cancelAndIgnoreRemainingEvents()
    }
    coVerify { apiEndpoint() }
  }

  suspend fun testErrorResponse(
    apiEndpoint: suspend () -> Response<*>,
    errorResponse: Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>,
    expectedErrorMessage: String
  ) {
    coEvery { apiEndpoint() } returns errorResponse

    dataSourceEndpointCall().test {
      val firstItem = awaitItem()
      assertTrue(firstItem is NetworkResult.Loading)

      var secondItem = awaitItem()
      assertTrue(secondItem is NetworkResult.Error)
      val errorResult = secondItem as NetworkResult.Error
      assertEquals(expectedErrorMessage, errorResult.message)

      cancelAndIgnoreRemainingEvents()
    }
    coVerify { apiEndpoint() }
  }

  suspend fun testError404Response(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>
  ) {
    val error404Response: Response<MovieTvCreditsResponse> = Response.error(
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

  suspend fun testUnknownHostExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = UnknownHostException("Unable to resolve host"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "Unable to resolve server hostname. Please check your internet connection."
    )
  }

  suspend fun testSocketTimeoutExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = SocketTimeoutException("Timeout"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "Connection timed out. Please try again."
    )
  }

  suspend fun testHttpExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>
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

  suspend fun testIOExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = IOException("Network error"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "Please check your network connection"
    )
  }

  suspend fun testGeneralExceptionResponse(
    apiEndpoint: suspend () -> Response<*>,
    dataSourceEndpointCall: suspend () -> Flow<NetworkResult<*>>
  ) {
    testErrorResponse(
      apiEndpoint = { apiEndpoint() },
      exception = Exception("Unexpected error"),
      dataSourceEndpointCall = { dataSourceEndpointCall() },
      "An unknown error occurred"
    )
  }

  suspend fun testPagingSource(
    mockResults: MovieTvResponse,
    mockApiCall: suspend () -> MovieTvResponse,
    loader: suspend () -> LoadResult<Int, ResultItemResponse>,
    resultAssertions: (LoadResult.Page<Int, ResultItemResponse>) -> Unit
  ) {
    coEvery { mockApiCall() } returns mockResults

    val result = loader()
    assert(result is LoadResult.Page)

    val page = result as LoadResult.Page
    resultAssertions(page)
    coVerify { mockApiCall() }
  }

  suspend fun testPagingSearchSource(
    mockResults: MultiSearchResponse,
    mockApiCall: suspend () -> MultiSearchResponse,
    loader: suspend () -> LoadResult<Int, ResultsItemSearchResponse>,
    resultAssertions: (LoadResult.Page<Int, ResultsItemSearchResponse>) -> Unit
  ) {
    coEvery { mockApiCall() } returns mockResults

    val result = loader()
    assert(result is LoadResult.Page)

    val page = result as LoadResult.Page
    resultAssertions(page)
    coVerify { mockApiCall() }
  }

  fun defaultMovieTvResponse(list: List<ResultItemResponse>) = MovieTvResponse(
    page = 1,
    results = list,
    totalResults = 2,
    totalPages = 3
  )

  fun defaultMultiSearchResponse(list: List<ResultsItemSearchResponse>) = MultiSearchResponse(
    page = 1,
    results = list,
    totalResults = 3,
    totalPages = 3
  )

  suspend fun Flow<PagingData<ResultItemResponse>>.testPagingFlow(
    testScope: TestScope,
    itemAssertions: (List<ResultItemResponse>) -> Unit
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

  suspend fun Flow<PagingData<ResultsItemSearchResponse>>.testPagingFlowSearch(
    testScope: TestScope,
    itemAssertions: (List<ResultsItemSearchResponse>) -> Unit
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
