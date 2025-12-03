package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OMDbViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun getScoreOMDb_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getOMDbDetailUseCase.getOMDbDetails(imdbId) } returns
      successFlow(mockOmdb)

    testViewModelFlow(
      runBlock = { viewModel.getOMDbDetails(imdbId) },
      liveData = viewModel.omdbResult,
      expectedSuccess = mockOmdb,
      verifyBlock = { coVerify { getOMDbDetailUseCase.getOMDbDetails(imdbId) } },
    )
  }

  @Test
  fun getScoreOMDb_whenUnsuccessful_emitsError() = runTest {
    coEvery { getOMDbDetailUseCase.getOMDbDetails(imdbId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getOMDbDetails(imdbId) },
      liveData = viewModel.omdbResult,
      expectError = errorMessage,
      verifyBlock = { coVerify { getOMDbDetailUseCase.getOMDbDetails(imdbId) } }
    )
  }

  @Test
  fun getScoreOMDb_whenLoading_doesNothing() = runTest {
    coEvery { getOMDbDetailUseCase.getOMDbDetails(imdbId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getOMDbDetails(imdbId) },
      liveData = viewModel.omdbResult,
      verifyBlock = { coVerify { getOMDbDetailUseCase.getOMDbDetails(imdbId) } }
    )
  }
}
