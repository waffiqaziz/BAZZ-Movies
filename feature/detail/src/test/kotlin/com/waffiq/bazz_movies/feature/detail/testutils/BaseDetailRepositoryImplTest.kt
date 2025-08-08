package com.waffiq.bazz_movies.feature.detail.testutils

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.feature.detail.data.repository.DetailRepositoryImpl
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.differ
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class BaseDetailRepositoryImplTest {

  protected lateinit var repository: DetailRepositoryImpl
  protected val movieDataSource: MovieDataSource = mockk()
  protected val id = 1
  protected val errorMessage = "Error occurred"

  protected val networkResultError = NetworkResult.Error(errorMessage)

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @BeforeTest
  fun setUp() {
    repository = DetailRepositoryImpl(movieDataSource)
  }

  /**
   * Generic test for successful network calls with flexible parameters
   */
  protected suspend fun <T, R> testSuccessfulCall(
    mockResponse: T,
    dataSourceCall: suspend () -> Flow<NetworkResult<T>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    expectedData: R,
    verifyDataSourceCall: () -> Unit,
  ) {
    val networkResult = NetworkResult.Success(mockResponse)

    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResult)
    }

    val result = repositoryCall().toList()
    assert(result[1] is Outcome.Success)

    val successResult = result[1] as Outcome.Success
    assertEquals(expectedData, successResult.data)

    verifyDataSourceCall()
  }

  /**
   * Generic test for unsuccessful network calls with flexible parameters
   */
  protected suspend fun <R> testUnsuccessfulCall(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyDataSourceCall: () -> Unit,
  ) {
    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
      emit(networkResultError)
    }

    val result = repositoryCall().toList()
    assert(result[1] is Outcome.Error)

    val errorResult = result[1] as Outcome.Error
    assertEquals(errorMessage, errorResult.message)

    verifyDataSourceCall()
  }

  /**
   * Generic test for loading state with flexible parameters
   */
  protected suspend fun <R> testLoadingState(
    dataSourceCall: suspend () -> Flow<NetworkResult<*>>,
    repositoryCall: suspend () -> Flow<Outcome<R>>,
    verifyDataSourceCall: () -> Unit,
  ) {
    coEvery { dataSourceCall() } returns flow {
      emit(NetworkResult.Loading)
    }

    val result = repositoryCall().first()
    assert(result is Outcome.Loading)

    verifyDataSourceCall()
  }

  /**
   * Generic test for successful paging data with non-empty results
   */
  protected fun testSuccessfulPagingData(
    mockPagingData: PagingData<MediaResponseItem>,
    dataSourceCall: suspend () -> Flow<PagingData<MediaResponseItem>>,
    repositoryCall: suspend () -> Flow<PagingData<MediaItem>>,
    verifyDataSourceCall: () -> Unit,
  ) = runTest {
    coEvery { dataSourceCall() } returns flowOf(mockPagingData)

    repositoryCall().test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val listMediaItem = differ.snapshot().items
      assertTrue(listMediaItem.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verifyDataSourceCall()
  }

  /**
   * Generic test for successful paging data with empty results
   */
  protected fun testEmptyPagingData(
    dataSourceCall: suspend () -> Flow<PagingData<MediaResponseItem>>,
    repositoryCall: suspend () -> Flow<PagingData<MediaItem>>,
    verifyDataSourceCall: () -> Unit,
  ) = runTest {
    val emptyPagingData = PagingData.from(emptyList<MediaResponseItem>())
    coEvery { dataSourceCall() } returns flowOf(emptyPagingData)

    repositoryCall().test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      assertTrue(differ.snapshot().items.isEmpty())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verifyDataSourceCall()
  }

  /**
   * Generic test for paging data with mock/invalid items
   */
  protected fun testPagingDataWithMockItems(
    dataSourceCall: suspend () -> Flow<PagingData<MediaResponseItem>>,
    repositoryCall: suspend () -> Flow<PagingData<MediaItem>>,
    verifyDataSourceCall: () -> Unit,
  ) = runTest {
    val invalidItem = mockk<MediaResponseItem>(relaxed = true)
    val pagingDataWithMock = PagingData.from(listOf(invalidItem))

    coEvery { dataSourceCall() } returns flowOf(pagingDataWithMock)

    repositoryCall().test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      assertTrue(differ.snapshot().items.isEmpty().not())
      job.cancel()

      cancelAndIgnoreRemainingEvents()
    }

    verifyDataSourceCall()
  }

  /**
   * Creates sample test data for paging tests
   */
  protected fun createSampleMediaItemResponse(
    id: Int = 1,
    name: String = "Test Name",
  ): MediaResponseItem = MediaResponseItem(id = id, name = name)

  /**
   * Creates paging data with sample items
   */
  protected fun createSamplePagingData(
    vararg items: MediaResponseItem,
  ): PagingData<MediaResponseItem> = PagingData.from(items.toList())
}
