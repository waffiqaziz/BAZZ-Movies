package com.waffiq.bazz_movies.core.data.testutils

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.data.domain.repository.IAccountRepository
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.data.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.data.domain.repository.ITrendingRepository
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.ERROR_MESSAGE
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.USER_REGION
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.movieMediaItem
import com.waffiq.bazz_movies.core.data.testutils.TestVariables.tvMediaItem
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.differ
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule

abstract class BaseInteractorTest {

  private val differ = differ<MediaItem>()
  protected val mockAccountRepository: IAccountRepository = mockk()
  protected val mockMoviesRepository: IMoviesRepository = mockk()
  protected val mockTvRepository: ITvRepository = mockk()
  protected val mockUserRepository: IUserRepository = mockk()
  protected val mockTrendingRepository: ITrendingRepository = mockk()

  protected val fakeMoviePagingData =
    PagingData.from(listOf(movieMediaItem, movieMediaItem, movieMediaItem))
  protected val fakeTvPagingData =
    PagingData.from(listOf(tvMediaItem, tvMediaItem, tvMediaItem))

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @Before
  open fun setup() {
    every { mockUserRepository.getUserRegionPref() } returns flowOf(USER_REGION)
  }

  protected fun testPagingData(
    mockCall: () -> Flow<PagingData<MediaItem>>,
    pagingData: PagingData<MediaItem>,
    interactorCall: () -> Flow<PagingData<MediaItem>>,
    assertions: (List<MediaItem>) -> Unit,
  ) = runTest {
    every { mockCall() } returns flowOf(pagingData)

    interactorCall().test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertions(pagingList)

      job.cancel()
      awaitComplete()
    }

    verify { mockCall() }
  }

  /**
   * Generic test for successful network calls with flexible parameters
   */
  protected suspend fun <T> testSuccessScenario(
    mockCall: suspend () -> Flow<Outcome<T>>,
    mockResponse: T,
    interactorCall: suspend () -> Flow<Outcome<T>>,
    additionalAssertions: (Outcome.Success<T>) -> Unit = {},
  ) {
    coEvery { mockCall() } returns flowOf(Outcome.Success(mockResponse))

    interactorCall().test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      additionalAssertions(emission as Outcome.Success<T>)
      awaitComplete()
    }

    coVerify { mockCall() }
  }

  /**
   * Generic test for error scenarios with flexible parameters
   */
  protected suspend fun <T> testErrorScenario(
    mockCall: suspend () -> Flow<Outcome<T>>,
    interactorCall: suspend () -> Flow<Outcome<T>>,
  ) {
    coEvery { mockCall() } returns flowOf(Outcome.Error(ERROR_MESSAGE))

    interactorCall().test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockCall() }
  }

  /**
   * Generic test for loading scenarios with flexible parameters
   */
  protected suspend fun <T> testLoadingScenario(
    mockCall: suspend () -> Flow<Outcome<T>>,
    interactorCall: suspend () -> Flow<Outcome<T>>,
  ) {
    coEvery { mockCall() } returns flowOf(Outcome.Loading)

    interactorCall().test {
      assertTrue(awaitItem() is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockCall() }
  }
}
