package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.PostTestHelper
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PostRateViewModelTest : BaseMediaDetailViewModelTest(), PostTestHelper {

  private val rate = 9.0f

  @Test
  fun postMovieRate_whenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postMovieRate(sessionId, rate, movieId) } returns
      flowSuccessWithLoading(mockPost)

    testViewModelFlowEvent(
      runBlock = { viewModel.postMovieRate(sessionId, rate, movieId) },
      liveData = viewModel.rateState,
      expectedSuccess = Event(true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postMovieRate(sessionId, rate, movieId) }
      },
    )
  }

  @Test
  fun postMovieRate_whenUnsuccessful_emitsError() = runTest {
    coEvery { postMethodUseCase.postMovieRate(sessionId, rate, movieId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postMovieRate(sessionId, rate, movieId) },
      liveData = viewModel.rateState,
      expectError = errorMessage,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postMovieRate(sessionId, rate, movieId) }
      }
    )
  }

  @Test
  fun postMovieRate_whenLoading_doesNothing() = runTest {
    coEvery { postMethodUseCase.postMovieRate(sessionId, rate, movieId) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postMovieRate(sessionId, rate, movieId) },
      liveData = viewModel.rateState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postMovieRate(sessionId, rate, movieId) }
      }
    )
  }

  @Test
  fun postTvRate_whenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postTvRate(sessionId, rate, tvId) } returns
      flowSuccessWithLoading(mockPost)

    testViewModelFlowEvent(
      runBlock = { viewModel.postTvRate(sessionId, rate, tvId) },
      liveData = viewModel.rateState,
      expectedSuccess = Event(true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postTvRate(sessionId, rate, tvId) }
      },
    )
  }

  @Test
  fun postTvRate_whenUnsuccessful_emitsError() = runTest {
    coEvery { postMethodUseCase.postTvRate(sessionId, rate, tvId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postTvRate(sessionId, rate, tvId) },
      liveData = viewModel.rateState,
      expectError = errorMessage,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postTvRate(sessionId, rate, tvId) }
      }
    )
  }

  @Test
  fun postTvRate_whenLoading_doesNothing() = runTest {
    coEvery { postMethodUseCase.postTvRate(sessionId, rate, tvId) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postTvRate(sessionId, rate, tvId) },
      liveData = viewModel.rateState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postTvRate(sessionId, rate, tvId) }
      }
    )
  }
}
