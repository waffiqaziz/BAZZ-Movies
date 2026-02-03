package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailTvViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun getTvDetail_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getTvDataWithUserPrefUseCase.getTvDetailWithUserRegion(tvId) } returns
      successFlow(mockMediaDetail)

    testViewModelFlow(
      runBlock = { viewModel.getTvDetail(tvId) },
      liveData = viewModel.detailMedia,
      expectedSuccess = mockMediaDetail,
      verifyBlock = { coVerify { getTvDataWithUserPrefUseCase.getTvDetailWithUserRegion(tvId) } },
    )
  }

  @Test
  fun getTvDetail_whenUnsuccessful_emitsError() = runTest {
    coEvery { getTvDataWithUserPrefUseCase.getTvDetailWithUserRegion(tvId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvDetail(tvId) },
      liveData = viewModel.detailMedia,
      expectError = errorMessage,
      verifyBlock = { coVerify { getTvDataWithUserPrefUseCase.getTvDetailWithUserRegion(tvId) } }
    )
  }

  @Test
  fun getTvDetail_whenLoading_doesNothing() = runTest {
    coEvery { getTvDataWithUserPrefUseCase.getTvDetailWithUserRegion(tvId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvDetail(tvId) },
      liveData = viewModel.detailMedia,
      verifyBlock = { coVerify { getTvDataWithUserPrefUseCase.getTvDetailWithUserRegion(tvId) } }
    )
  }

  @Test
  fun getTvTrailerLink_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getTvDetailUseCase.getTvTrailerLink(tvId) } returns
      successFlow(mockLinkVideo)

    testViewModelFlow(
      runBlock = { viewModel.getTvTrailerLink(tvId) },
      liveData = viewModel.linkVideo,
      expectedSuccess = mockLinkVideo,
      verifyBlock = { coVerify { getTvDetailUseCase.getTvTrailerLink(tvId) } },
    )
  }

  @Test
  fun getTvTrailerLink_whenUnsuccessful_emitsError() = runTest {
    coEvery { getTvDetailUseCase.getTvTrailerLink(tvId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvTrailerLink(tvId) },
      liveData = viewModel.linkVideo,
      expectError = errorMessage,
      verifyBlock = { coVerify { getTvDetailUseCase.getTvTrailerLink(tvId) } }
    )
  }

  @Test
  fun getTvTrailerLink_whenLoading_doesNothing() = runTest {
    coEvery { getTvDetailUseCase.getTvTrailerLink(tvId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvTrailerLink(tvId) },
      liveData = viewModel.detailMedia,
      verifyBlock = { coVerify { getTvDetailUseCase.getTvTrailerLink(tvId) } }
    )
  }

  @Test
  fun getTvCredits_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getTvDetailUseCase.getTvCredits(tvId) } returns
      successFlow(mockMediaCredits)

    testViewModelFlow(
      runBlock = { viewModel.getTvCredits(tvId) },
      liveData = viewModel.mediaCredits,
      expectedSuccess = mockMediaCredits,
      verifyBlock = { coVerify { getTvDetailUseCase.getTvCredits(tvId) } },
    )
  }

  @Test
  fun getTvCredits_whenUnsuccessful_emitsError() = runTest {
    coEvery { getTvDetailUseCase.getTvCredits(tvId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvCredits(tvId) },
      liveData = viewModel.mediaCredits,
      expectError = errorMessage,
      verifyBlock = { coVerify { getTvDetailUseCase.getTvCredits(tvId) } }
    )
  }

  @Test
  fun getTvCredits_whenLoading_doesNothing() = runTest {
    coEvery { getTvDetailUseCase.getTvCredits(tvId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvCredits(tvId) },
      liveData = viewModel.mediaCredits,
      verifyBlock = { coVerify { getTvDetailUseCase.getTvCredits(tvId) } }
    )
  }

  @Test
  fun getTvState_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } returns
      successFlow(mockMediaStated)

    testViewModelFlow(
      runBlock = { viewModel.getTvState(tvId) },
      liveData = viewModel.itemState,
      expectedSuccess = mockMediaStated,
      verifyBlock = { coVerify { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } },
    )
  }

  @Test
  fun getTvState_whenUnsuccessful_emitsError() = runTest {
    coEvery { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvState(tvId) },
      liveData = viewModel.itemState,
      expectError = errorMessage,
      verifyBlock = { coVerify { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } }
    )
  }

  @Test
  fun getTvState_whenLoading_doesNothing() = runTest {
    coEvery { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getTvState(tvId) },
      liveData = viewModel.itemState,
      verifyBlock = { coVerify { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } }
    )
  }

  @Test
  fun getTvWatchProviders_withNullFields_triggersOrEmptyBranches() = runTest {
    coEvery { getTvDataWithUserPrefUseCase.getTvWatchProvidersWithUserRegion(tvId) } returns
      successFlow(nullProvider)

    viewModel.getTvWatchProviders(tvId)
    advanceUntilIdle()

    assertThat(viewModel.watchProvidersUiState.value).isEqualTo(
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
      getTvDataWithUserPrefUseCase.getTvWatchProvidersWithUserRegion(tvId)
    } returns successFlow(fullProvider)

    viewModel.getTvWatchProviders(tvId)
    advanceUntilIdle()

    assertThat(viewModel.watchProvidersUiState.value)
      .isInstanceOf(WatchProvidersUiState.Success::class.java)
  }

  @Test
  fun getTvRecommendation_whenSuccessful_emitsPagingData() {
    coEvery { getTvDetailUseCase.getTvRecommendationPagingData(tvId) } returns
      flowOf(PagingData.from(listOf(mockMediaItem)))

    testPagingLiveData(
      liveData = viewModel.recommendation,
      runBlock = { viewModel.getTvRecommendation(tvId) },
      itemAssertions = { snapshot ->
        assertThat(snapshot).isNotEmpty()
      }
    )
  }

  @Test
  fun getTvAllScore_whenSuccessful_emitsSuccess() {
    coEvery { getTvAllScoreUseCase.getTvAllScore(tvId) } returns
      successFlow(mockOmdb)

    testViewModelFlow(
      runBlock = { viewModel.getTvAllScore(tvId) },
      liveData = viewModel.omdbResult,
      expectedSuccess = mockOmdb,
      verifyBlock = { coVerify { getTvAllScoreUseCase.getTvAllScore(tvId) } }
    )
  }
}
