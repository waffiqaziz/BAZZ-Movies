package com.waffiq.bazz_movies.feature.detail.testutils

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailUseCase
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.differ
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

/**
 * Base class for interactor tests, providing common setup and utility methods.
 */
abstract class BaseInteractorTest {

  protected val mockRepository: IDetailRepository = mockk()

  // for composite interactor tests
  protected val mockGetMovieDetailUseCase: GetMovieDetailUseCase = mockk()
  protected val mockGetTvDetailUseCase: GetTvDetailUseCase = mockk()
  protected val mockGetMovieStateUseCase: GetMovieStateUseCase = mockk()
  protected val mockGetTvStateUseCase: GetTvStateUseCase = mockk()
  protected val mockUserPrefUseCase: UserPrefUseCase = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  protected abstract fun initInteractor()

  @Before
  open fun baseSetUp() {
    initInteractor()
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

  /**
   * Generic test for paging data scenarios with flexible parameters
   */
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
}
