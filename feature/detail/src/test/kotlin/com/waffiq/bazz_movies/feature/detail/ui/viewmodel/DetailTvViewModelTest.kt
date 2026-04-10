package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.omdbDetails
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailTvViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun getTvDetail_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvDetailWithUserRegion(tvId) } returns
      successFlow(mockMediaDetail)

    testViewModelState(
      runBlock = { viewModel.getTvDetail(tvId) },
      stateSelector = { it.detail },
      expectedStates = listOf(mockMediaDetail),
      verifyBlock = {
        verify(exactly = 1) { mockGetMediaDetailUseCase.getTvDetailWithUserRegion(tvId) }
      },
    )
  }

  @Test
  fun getTvDetail_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvDetailWithUserRegion(tvId) } returns errorFlow

    testViewModelState(
      runBlock = { viewModel.getTvDetail(tvId) },
      stateSelector = { it.detail },
      expectedErrors = listOf(errorMessage),
      verifyBlock = {
        verify(exactly = 1) { mockGetMediaDetailUseCase.getTvDetailWithUserRegion(tvId) }
      }
    )
  }

  @Test
  fun getTvDetail_whenLoading_doesNothing() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvDetailWithUserRegion(tvId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getTvDetail(tvId) },
      stateSelector = { it.detail },
      verifyBlock = {
        coVerify { mockGetMediaDetailUseCase.getTvDetailWithUserRegion(tvId) }
      }
    )
  }

  @Test
  fun getTvTrailerLink_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvTrailerLink(tvId) } returns
      successFlow(mockVideoLink)

    testViewModelState(
      runBlock = { viewModel.getTvTrailerLink(tvId) },
      stateSelector = { it.videoLink },
      expectedStates = listOf(mockVideoLink),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getTvTrailerLink(tvId) } },
    )
  }

  @Test
  fun getTvTrailerLink_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvTrailerLink(tvId) } returns errorFlow

    testViewModelState(
      runBlock = { viewModel.getTvTrailerLink(tvId) },
      stateSelector = { it.videoLink },
      expectedErrors = listOf(errorMessage),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getTvTrailerLink(tvId) } }
    )
  }

  @Test
  fun getTvTrailerLink_whenLoading_doesNothing() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvTrailerLink(tvId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getTvTrailerLink(tvId) },
      stateSelector = { it.videoLink },
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getTvTrailerLink(tvId) } }
    )
  }

  @Test
  fun getTvCredits_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvCredits(tvId) } returns
      successFlow(mockMediaCredits)

    testViewModelState(
      runBlock = { viewModel.getTvCredits(tvId) },
      stateSelector = { it.credits },
      expectedStates = listOf(mockMediaCredits),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getTvCredits(tvId) } },
    )
  }

  @Test
  fun getTvCredits_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvCredits(tvId) } returns errorFlow

    testViewModelState(
      runBlock = { viewModel.getTvCredits(tvId) },
      stateSelector = { it.credits },
      expectedErrors = listOf(errorMessage),
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getTvCredits(tvId) } }
    )
  }

  @Test
  fun getTvCredits_whenLoading_doesNothing() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvCredits(tvId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getTvCredits(tvId) },
      stateSelector = { it.credits },
      verifyBlock = { coVerify { mockGetMediaDetailUseCase.getTvCredits(tvId) } }
    )
  }

  @Test
  fun getTvState_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockMediaStateUseCase.getTvStateWithUser(tvId) } returns
      successFlow(mockMediaStated)

    testViewModelState(
      runBlock = { viewModel.getTvState(tvId) },
      stateSelector = { it.itemState },
      expectedStates = listOf(mockMediaStated),
      verifyBlock = { coVerify { mockMediaStateUseCase.getTvStateWithUser(tvId) } },
    )
  }

  @Test
  fun getTvState_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockMediaStateUseCase.getTvStateWithUser(tvId) } returns errorFlow

    testViewModelState(
      runBlock = { viewModel.getTvState(tvId) },
      stateSelector = { it.itemState },
      expectedErrors = listOf(errorMessage),
      verifyBlock = { coVerify { mockMediaStateUseCase.getTvStateWithUser(tvId) } }
    )
  }

  @Test
  fun getTvState_whenLoading_doesNothing() = runTest {
    coEvery { mockMediaStateUseCase.getTvStateWithUser(tvId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getTvState(tvId) },
      stateSelector = { it.itemState },
      verifyBlock = { coVerify { mockMediaStateUseCase.getTvStateWithUser(tvId) } }
    )
  }

  @Test
  fun getTvWatchProviders_withNullFields_triggersOrEmptyBranches() = runTest {
    coEvery { mockGetMediaDetailUseCase.getTvWatchProvidersWithUserRegion(tvId) } returns
      successFlow(nullProvider)

    viewModel.getTvWatchProviders(tvId)
    advanceUntilIdle()

    assertThat(viewModel.uiState.value.watchProviders).isEqualTo(
      WatchProvidersUiState
        .Success(
          emptyList(),
          emptyList(),
          emptyList(),
          emptyList(),
          emptyList()
        )
    )
  }

  @Test
  fun getTvWatchProviders_withNonNullFields_skipsOrEmptyBranches() = runTest {
    coEvery {
      mockGetMediaDetailUseCase.getTvWatchProvidersWithUserRegion(tvId)
    } returns successFlow(mockWatchProvider)

    viewModel.getTvWatchProviders(tvId)
    advanceUntilIdle()

    assertThat(viewModel.uiState.value.watchProviders)
      .isInstanceOf(WatchProvidersUiState.Success::class.java)
  }

  @Test
  fun getTvRecommendation_whenSuccessful_emitsPagingData() {
    coEvery { mockGetListTvUseCase.getTvRecommendation(tvId) } returns
      flowOf(PagingData.from(listOf(mockMediaItem)))

    testPagingState(
      pagingFlow = viewModel.recommendations,
      runBlock = { viewModel.getTvRecommendation(tvId) },
      itemAssertions = { snapshot ->
        assertThat(snapshot).containsExactly(mockMediaItem)
      },
    )
  }

  @Test
  fun getTvAllScore_whenSuccessful_emitsSuccess() {
    coEvery { mockGetOMDbDetailUseCase.getTvAllScore(tvId) } returns
      successFlow(omdbDetails)

    testViewModelState(
      runBlock = { viewModel.getTvAllScore(tvId) },
      stateSelector = { it.omdbDetails },
      expectedStates = listOf(omdbDetails),
      verifyBlock = { coVerify { mockGetOMDbDetailUseCase.getTvAllScore(tvId) } }
    )
  }

  @Test
  fun getTvAllScore_whenLoading_emitsLoading() {
    coEvery { mockGetOMDbDetailUseCase.getTvAllScore(tvId) } returns loadingFlow

    testViewModelState(
      runBlock = { viewModel.getTvAllScore(tvId) },
      stateSelector = { it.omdbDetails },
      expectedLoadingStates = listOf(true),
      verifyBlock = { coVerify { mockGetOMDbDetailUseCase.getTvAllScore(tvId) } }
    )
  }
}
