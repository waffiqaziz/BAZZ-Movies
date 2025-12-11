package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.SESSION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.PostTestHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostRateViewModelTest : BaseMediaDetailViewModelTest(), PostTestHelper {

  private val rate = 9.0f

  @Before
  override fun setup() {
    super.setup()
    every { mockUserPrefUseCase.getUserToken() } returns flowOf(SESSION_ID)
  }

  @Test
  fun postMovieRate_whenSuccessful_emitsSuccess() = runTest {
    coEvery { postRateUseCase.postMovieRate(rate, movieId) } returns
      flowSuccessWithLoading(mockPost)

    testViewModelFlowEvent(
      runBlock = { viewModel.postMovieRate(rate, movieId) },
      liveData = viewModel.rateState,
      expectedSuccess = Event(true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postRateUseCase.postMovieRate(rate, movieId) }
      },
    )
  }

  @Test
  fun postMovieRate_whenUnsuccessful_emitsError() = runTest {
    coEvery { postRateUseCase.postMovieRate(rate, movieId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postMovieRate(rate, movieId) },
      liveData = viewModel.rateState,
      expectError = errorMessage,
      checkLoading = true,
      verifyBlock = {
        coVerify { postRateUseCase.postMovieRate(rate, movieId) }
      }
    )
  }

  @Test
  fun postMovieRate_whenLoading_doesNothing() = runTest {
    coEvery { postRateUseCase.postMovieRate(rate, movieId) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postMovieRate(rate, movieId) },
      liveData = viewModel.rateState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postRateUseCase.postMovieRate(rate, movieId) }
      }
    )
  }

  @Test
  fun postTvRate_whenSuccessful_emitsSuccess() = runTest {
    coEvery { postRateUseCase.postTvRate(rate, tvId) } returns
      flowSuccessWithLoading(mockPost)

    testViewModelFlowEvent(
      runBlock = { viewModel.postTvRate(rate, tvId) },
      liveData = viewModel.rateState,
      expectedSuccess = Event(true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postRateUseCase.postTvRate(rate, tvId) }
      },
    )
  }

  @Test
  fun postTvRate_whenUnsuccessful_emitsError() = runTest {
    coEvery { postRateUseCase.postTvRate(rate, tvId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postTvRate(rate, tvId) },
      liveData = viewModel.rateState,
      expectError = errorMessage,
      checkLoading = true,
      verifyBlock = {
        coVerify { postRateUseCase.postTvRate(rate, tvId) }
      }
    )
  }

  @Test
  fun postTvRate_whenLoading_doesNothing() = runTest {
    coEvery { postRateUseCase.postTvRate(rate, tvId) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postTvRate(rate, tvId) },
      liveData = viewModel.rateState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postRateUseCase.postTvRate(rate, tvId) }
      }
    )
  }
}
