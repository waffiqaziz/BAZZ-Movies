package com.waffiq.bazz_movies.feature.detail.testutils

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.data.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.ERROR_MESSAGE
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule

/**
 * Base class for interactor tests, providing common setup and utility methods.
 */
abstract class BaseInteractorTest {

  protected val mockDetailRepository: IDetailRepository = mockk()
  protected val mockMoviesRepository: IMoviesRepository = mockk()
  protected val mockTvRepository: ITvRepository = mockk()
  protected val mockUserRepository: IUserRepository = mockk()

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
}
