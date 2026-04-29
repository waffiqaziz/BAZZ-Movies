package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
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
      coEvery { mockGetOMDbDetailUseCase.getOMDbDetails(any()) } returns
        successFlow(mockOmdb)

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
        expectedStates = listOf(
          mockMediaDetail.copy(imdbId = ""),
        ),
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
  fun getMovieVideoLinks_whenSuccessful_emitsSuccess() {
    coEvery { mockGetMediaDetailUseCase.getMovieVideoLinks(movieId) } returns
      successFlow(mockVideoLink)

    testViewModelState(
      runBlock = { viewModel.getMovieVideoLink(movieId) },
      stateSelector = { it.videoLink },
      expectedStates = listOf(mockVideoLink),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getMovieVideoLinks(movieId) } },
    )
  }

  @Test
  fun getMovieVideoLinks_whenUnsuccessful_emitsError() {
    coEvery { mockGetMediaDetailUseCase.getMovieVideoLinks(movieId) } returns errorFlow

    testViewModelState(
      runBlock = { viewModel.getMovieVideoLink(movieId) },
      stateSelector = { it.videoLink },
      expectedErrors = listOf(errorMessage),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getMovieVideoLinks(movieId) } },
    )
  }

  @Test
  fun getMovieVideoLinks_whenLoading_doesNothing() {
    coEvery { mockGetMediaDetailUseCase.getMovieVideoLinks(movieId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getMovieVideoLink(movieId) },
      stateSelector = { it.videoLink },
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getMovieVideoLinks(movieId) } },
    )
  }

  @Test
  fun getMovieCredits_whenSuccessful_emitsSuccess() {
    coEvery { mockGetMediaDetailUseCase.getMovieCredits(movieId) } returns
      successFlow(mockMediaCredits)

    testViewModelState(
      runBlock = { viewModel.getMovieCredits(movieId) },
      stateSelector = { it.credits },
      expectedStates = listOf(mockMediaCredits),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getMovieCredits(movieId) } },
    )
  }

  @Test
  fun getMovieCredits_whenUnsuccessful_emitsError() {
    coEvery { mockGetMediaDetailUseCase.getMovieCredits(movieId) } returns errorFlow

    testViewModelState(
      runBlock = { viewModel.getMovieCredits(movieId) },
      stateSelector = { it.credits },
      expectedErrors = listOf(errorMessage),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getMovieCredits(movieId) } },
    )
  }

  @Test
  fun getMovieCredits_whenLoading_doesNothing() {
    coEvery { mockGetMediaDetailUseCase.getMovieCredits(movieId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getMovieCredits(movieId) },
      stateSelector = { it.credits },
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getMovieCredits(movieId) } },
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
  fun getMovieWatchProviders_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery {
        mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId)
      } returns successFlow(mockWatchProvider)

      testViewModelState(
        runBlock = { viewModel.getMovieWatchProviders(movieId) },
        stateSelector = { it.watchProviders },
        expectedStates = listOf(WatchProvidersUiState.Loading, mockWatchProviderState),
        verifyBlock = {
          coVerify { mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId) }
        },
      )
    }

  @Test
  fun getMovieWatchProviders_whenUnsuccessful_emitsError() =
    runTest {
      coEvery {
        mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId)
      } returns errorFlow

      testViewModelState(
        runBlock = { viewModel.getMovieWatchProviders(movieId) },
        stateSelector = { it.watchProviders },
        expectedStates = listOf(
          WatchProvidersUiState.Loading,
          WatchProvidersUiState.Error(errorMessage),
        ),
        verifyBlock = {
          coVerify { mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId) }
        },
      )
    }

  @Test
  fun getMovieWatchProviders_whenLoading_emitsLoading() =
    runTest {
      coEvery {
        mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId)
      } returns loadingFlow

      testViewModelState(
        runBlock = { viewModel.getMovieWatchProviders(movieId) },
        stateSelector = { it.watchProviders },
        expectedStates = listOf(WatchProvidersUiState.Loading),
        verifyBlock = {
          coVerify { mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId) }
        },
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
  fun getMovieWatchProviders_withNullFields_triggersOrEmptyBranches() =
    runTest {
      coEvery {
        mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId)
      } returns successFlow(nullProvider)

      viewModel.getMovieWatchProviders(movieId)
      advanceUntilIdle()

      assertThat(viewModel.uiState.value.watchProviders).isEqualTo(
        WatchProvidersUiState
          .Success(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
          ),
      )
    }

  @Test
  fun getMovieWatchProviders_withNonNullFields_skipsOrEmptyBranches() =
    runTest {
      coEvery {
        mockGetMediaDetailUseCase.getMovieWatchProvidersWithUserRegion(movieId)
      } returns successFlow(mockWatchProvider)

      viewModel.getMovieWatchProviders(movieId)
      advanceUntilIdle()

      assertThat(viewModel.uiState.value.watchProviders)
        .isInstanceOf(WatchProvidersUiState.Success::class.java)
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
