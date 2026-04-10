package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OMDbViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun getScoreOMDb_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) } returns
      successFlow(mockOmdb)

    testViewModelState(
      runBlock = { viewModel.getOMDbDetails(imdbId) },
      stateSelector = { it.omdbDetails },
      expectedStates = listOf(mockOmdb),
      verifyBlock = { coVerify { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) } },
    )
  }

  @Test
  fun getScoreOMDb_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) } returns errorFlow

    testViewModelState(
      runBlock = { viewModel.getOMDbDetails(imdbId) },
      stateSelector = { it.omdbDetails },
      expectedErrors = listOf(errorMessage),
      verifyBlock = { coVerify { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) } }
    )
  }

  @Test
  fun getScoreOMDb_whenLoading_doesNothing() = runTest {
    coEvery { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getOMDbDetails(imdbId) },
      stateSelector = { it.omdbDetails },
      verifyBlock = { coVerify { mockGetOMDbDetailUseCase.getOMDbDetails(imdbId) } }
    )
  }
}
