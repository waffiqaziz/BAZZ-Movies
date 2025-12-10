package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.launchAndHandleOutcome
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LaunchAndHandleOutcomeTest {

  val onSuccess = mockk<(String) -> Unit>(relaxed = true)
  val onError = mockk<(String) -> Unit>(relaxed = true)
  val onLoading = mockk<() -> Unit>(relaxed = true)

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val testDispatcher = StandardTestDispatcher()

  private lateinit var viewModel: TestViewModel

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    viewModel = TestViewModel()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun launchAndHandleOutcome_whenSuccessOutcome_invokesOnSuccess() = runTest {
    val successData = "Test Data"
    val flow = flowOf(Outcome.Success(successData))

    viewModel.launchAndHandleOutcome(
      flow = flow,
      onSuccess = onSuccess,
      onError = onError
    )
    advanceUntilIdle()

    verify(exactly = 1) { onSuccess(successData) }
    verify(exactly = 0) { onError(any()) }
  }

  @Test
  fun launchAndHandleOutcome_whenErrorOutcome_invokesOnError() = runTest {
    val errorMessage = "Error occurred"
    val flow = flowOf(Outcome.Error(errorMessage))

    viewModel.launchAndHandleOutcome(
      flow = flow,
      onSuccess = onSuccess,
      onError = onError
    )
    advanceUntilIdle()

    verify(exactly = 0) { onSuccess(any()) }
    verify(exactly = 1) { onError(errorMessage) }
  }

  @Test
  fun launchAndHandleOutcome_whenLoadingOutcome_invokesOnLoading() = runTest {
    val flow = flowOf(Outcome.Loading)

    viewModel.launchAndHandleOutcome(
      flow = flow,
      onSuccess = onSuccess,
      onError = onError,
      onLoading = onLoading
    )
    advanceUntilIdle()

    verify(exactly = 1) { onLoading() }
    verify(exactly = 0) { onSuccess(any()) }
    verify(exactly = 0) { onError(any()) }
  }

  @Test
  fun launchAndHandleOutcome_whenLoadingOutcomeAndOnLoadingIsNull_doesNotThrowException() =
    runTest {
      val flow = flowOf(Outcome.Loading)

      viewModel.launchAndHandleOutcome(
        flow = flow,
        onSuccess = onSuccess,
        onError = onError,
        onLoading = null
      )
      advanceUntilIdle()

      verify(exactly = 0) { onSuccess(any()) }
      verify(exactly = 0) { onError(any()) }
    }

  @Test
  fun launchAndHandleOutcome_whenMultipleOutcomes_invokesCallbacksInOrder() = runTest {
    val successData = "Final Data"
    val errorMessage = "Temporary Error"
    val flow = flowOf(
      Outcome.Loading,
      Outcome.Error(errorMessage),
      Outcome.Success(successData)
    )

    viewModel.launchAndHandleOutcome(
      flow = flow,
      onSuccess = onSuccess,
      onError = onError,
      onLoading = onLoading
    )
    advanceUntilIdle()

    verifySequence {
      onLoading()
      onError(errorMessage)
      onSuccess(successData)
    }
  }

  @Test
  fun launchAndHandleOutcome_whenComplexDataType_handlesSuccessCorrectly() = runTest {
    data class User(val id: Int, val name: String)

    val userData = User(1, "username")
    val flow = flowOf(Outcome.Success(userData))
    val onSuccess = mockk<(User) -> Unit>(relaxed = true)

    viewModel.launchAndHandleOutcome(
      flow = flow,
      onSuccess = onSuccess,
      onError = onError
    )
    advanceUntilIdle()

    verify(exactly = 1) { onSuccess(userData) }
  }

  class TestViewModel : ViewModel()
}
