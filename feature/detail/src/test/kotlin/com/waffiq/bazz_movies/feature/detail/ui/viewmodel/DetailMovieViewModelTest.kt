package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class DetailMovieViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun getMovieDetail_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) } returns
        successFlow(mockMediaDetail)
      setupGetOMDbDetailsMockReturnValue()

      testViewModelState(
        runBlock = { viewModel.getMovieDetail(movieId) },
        stateSelector = { it.detail },
        expectedStates = listOf(mockMediaDetail),
        verifyBlock = {
          verify(exactly = 1) { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) }
        },
      )
    }

  @Test
  fun getMovieDetail_whenImdbIsMissing_emitsSuccess() =
    runTest {
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) } returns
        successFlow(mockMediaDetail.copy(imdbId = null))
      setupGetOMDbDetailsMockReturnValue()

      testViewModelState(
        runBlock = { viewModel.getMovieDetail(movieId) },
        stateSelector = { it.detail },
        expectedStates = listOf(mockMediaDetail.copy(imdbId = null)),
        verifyBlock = {
          verify(exactly = 1) { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) }
        },
      )
    }

  @Test
  fun getMovieDetail_whenImdbIsEmpty_emitsSuccess() =
    runTest {
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) } returns
        successFlow(mockMediaDetail.copy(imdbId = ""))
      setupGetOMDbDetailsMockReturnValue()

      testViewModelState(
        runBlock = { viewModel.getMovieDetail(movieId) },
        stateSelector = { it.detail },
        expectedStates = listOf(mockMediaDetail.copy(imdbId = "")),
        verifyBlock = {
          verify(exactly = 1) { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) }
        },
      )
    }

  @Test
  fun getMovieDetail_whenImdbIsBlank_emitsSuccess() =
    runTest {
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) } returns
        successFlow(mockMediaDetail.copy(imdbId = " "))
      setupGetOMDbDetailsMockReturnValue()

      testViewModelState(
        runBlock = { viewModel.getMovieDetail(movieId) },
        stateSelector = { it.detail },
        expectedStates = listOf(
          mockMediaDetail.copy(imdbId = " "),
        ),
        verifyBlock = {
          verify(exactly = 1) { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) }
        },
      )
    }

  @Test
  fun getMovieDetail_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) } returns
        errorFlow

      testViewModelState(
        runBlock = { viewModel.getMovieDetail(movieId) },
        stateSelector = { it.detail },
        expectedErrors = listOf(errorMessage),
      )
    }

  @Test
  fun getMovieDetail_whenLoading_doesNothing() {
    every { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) } returns
      loadingFlow

    testViewModelState(
      runBlock = { viewModel.getMovieDetail(movieId) },
      stateSelector = { it.detail },
      expectedLoadingStates = listOf(true),
      verifyBlock = {
        verify(exactly = 1) { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) }
      },
    )
  }

  @Test
  fun getMovieState_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns
        successFlow(mockMediaStated)

      testViewModelState(
        runBlock = { viewModel.getMovieState(movieId) },
        stateSelector = { it.itemState },
        expectedStates = listOf(mockMediaStated),
        verifyBlock = { coVerify { mockMediaStateUseCase.getMovieStateWithUser(movieId) } },
      )
    }

  @Test
  fun getMovieState_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns errorFlow

      testViewModelState(
        runBlock = { viewModel.getMovieState(movieId) },
        stateSelector = { it.itemState },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockMediaStateUseCase.getMovieStateWithUser(movieId) } },
      )
    }

  @Test
  fun getMovieState_whenLoading_doesNothing() =
    runTest {
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns loadingFlow

      testViewModelState(
        runBlock = { viewModel.getMovieState(movieId) },
        stateSelector = { it.itemState },
        verifyBlock = { coVerify { mockMediaStateUseCase.getMovieStateWithUser(movieId) } },
      )
    }

  @Test
  fun getMovieRecommendation_whenSuccessful_emitsPagingData() {
    coEvery { mockGetListMoviesUseCase.getMovieRecommendation(movieId) } returns
      flowOf(PagingData.from(listOf(mockMediaItem)))

    testPagingState(
      pagingFlow = viewModel.recommendations,
      runBlock = { viewModel.getMovieRecommendation(movieId) },
      itemAssertions = { snapshot ->
        assertThat(snapshot).containsExactly(mockMediaItem)
      },
    )
  }

  @Test
  fun executeUseCase_defaultOnSuccess_shouldDoNothing() =
    runTest {
      val successFlow = flowOf(Outcome.Success("data"))
      var finallySuccessCalled = false

      viewModel.executeUseCase(
        flowProvider = { successFlow },
        onFinallySuccess = { finallySuccessCalled = true },
      )
      advanceUntilIdle()

      assertTrue(finallySuccessCalled)
    }
}
