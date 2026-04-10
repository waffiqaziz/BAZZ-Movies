package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_added_successfully
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.SESSION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.PostTestHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PostRateViewModelTest : BaseMediaDetailViewModelTest(), PostTestHelper {

  private val rate = 9.0f

  @Before
  override fun setup() {
    super.setup()
    every { mockUserPrefUseCase.getUserToken() } returns flowOf(SESSION_ID)
  }

  @Test
  fun postMovieRate_whenSuccessful_emitsSuccess() = runTest {
    // call to get media state first
    setupMediaStateSuccessful()
    testViewModelState(
      runBlock = {
        viewModel.getMovieState(movieId)
        advanceUntilIdle()
        viewModel.postMovieRate(rate, movieId)
      },
      stateSelector = { it.itemState },
      expectedStates = listOf(
        mockMediaStated,
        mockMediaStated.copy(rated = Rated.Value(rate.toDouble()))
      ),
      verifyBlock = {
        coVerify { mockPostRateUseCase.postMovieRate(rate, movieId) }
      },
    )
  }

  @Test
  fun toaseEvent_whenPostMovieRateSuccessful_emitsCorrectStringId() = runTest{
    setupMediaStateSuccessful()
    viewModel.toastEvent.test {
      // Trigger AFTER collecting
      viewModel.getMovieState(movieId)
      advanceUntilIdle()

      viewModel.postMovieRate(rate, movieId)

      val result = awaitItem()
      assertEquals(rating_added_successfully, result)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun postMovieRate_whenItemStateIsNull_itemStateRemainsNull() = runTest {
    coEvery { mockPostRateUseCase.postMovieRate(rate, movieId) } returns
      flowSuccessWithLoading(mockPost)

    viewModel.postMovieRate(rate, movieId)
    advanceUntilIdle()

    assertNull(viewModel.uiState.value.itemState)

    coVerify { mockPostRateUseCase.postMovieRate(rate, movieId) }
  }

  @Test
  fun postMovieRate_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockPostRateUseCase.postMovieRate(rate, movieId) } returns
      flowFailedWithLoading

    testViewModelState(
      runBlock = { viewModel.postMovieRate(rate, movieId) },
      stateSelector = { it.itemState },
      expectedErrors = listOf(errorMessage),
      verifyBlock = {
        coVerify { mockPostRateUseCase.postMovieRate(rate, movieId) }
      }
    )
  }

  @Test
  fun postMovieRate_whenLoading_doesNothing() = runTest {
    coEvery { mockPostRateUseCase.postMovieRate(rate, movieId) } returns
      loadingFlow

    testViewModelState(
      runBlock = { viewModel.postMovieRate(rate, movieId) },
      stateSelector = { it.itemState },
      expectedLoadingStates = listOf(true),
      verifyBlock = {
        coVerify { mockPostRateUseCase.postMovieRate(rate, movieId) }
      }
    )
  }

  @Test
  fun postTvRate_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockMediaStateUseCase.getTvStateWithUser(tvId) } returns
      successFlow(mockMediaStated)
    coEvery { mockPostRateUseCase.postTvRate(rate, tvId) } returns
      flowSuccessWithLoading(mockPost)

    testViewModelState(
      runBlock = {
        viewModel.getTvState(tvId)
        advanceUntilIdle()
        viewModel.postTvRate(rate, tvId)
      },
      stateSelector = { it.itemState },
      expectedStates = listOf(
        mockMediaStated,
        mockMediaStated.copy(rated = Rated.Value(rate.toDouble()))
      ),
      verifyBlock = {
        coVerify { mockPostRateUseCase.postTvRate(rate, tvId) }
      },
    )
  }

  @Test
  fun postTvRate_whenItemStateIsNull_itemStateRemainsNull() = runTest {
    coEvery { mockPostRateUseCase.postTvRate(rate, tvId) } returns
      flowSuccessWithLoading(mockPost)

    viewModel.postTvRate(rate, tvId)
    advanceUntilIdle()

    assertNull(viewModel.uiState.value.itemState)

    coVerify { mockPostRateUseCase.postTvRate(rate, tvId) }
  }

  @Test
  fun postTvRate_whenItemStateIsNull_stillEmitsToast() = runTest {
    coEvery { mockPostRateUseCase.postTvRate(rate, tvId) } returns
      flowSuccessWithLoading(mockPost)

    val toastEvents = mutableListOf<Int>()
    val collectJob = launch {
      viewModel.toastEvent.collect { toastEvents.add(it) }
    }

    viewModel.postTvRate(rate, tvId)
    advanceUntilIdle()

    assertEquals(rating_added_successfully, toastEvents.last())

    collectJob.cancel()
  }

  @Test
  fun postTvRate_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockPostRateUseCase.postTvRate(rate, tvId) } returns
      flowFailedWithLoading

    testViewModelState(
      runBlock = { viewModel.postTvRate(rate, tvId) },
      stateSelector = { it.itemState },
      expectedErrors = listOf(errorMessage),
      verifyBlock = {
        coVerify { mockPostRateUseCase.postTvRate(rate, tvId) }
      }
    )
  }

  @Test
  fun postTvRate_whenLoading_doesNothing() = runTest {
    coEvery { mockPostRateUseCase.postTvRate(rate, tvId) } returns
      loadingFlow

    testViewModelState(
      runBlock = { viewModel.postTvRate(rate, tvId) },
      stateSelector = { it.itemState },
      expectedLoadingStates = listOf(true),
      verifyBlock = {
        coVerify { mockPostRateUseCase.postTvRate(rate, tvId) }
      }
    )
  }

  private fun setupMediaStateSuccessful(){
    coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)
    coEvery { mockPostRateUseCase.postMovieRate(rate, movieId) } returns
      flowSuccessWithLoading(mockPost)
  }
}
