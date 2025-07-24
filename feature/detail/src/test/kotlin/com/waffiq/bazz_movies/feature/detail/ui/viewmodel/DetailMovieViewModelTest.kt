package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailMovieViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun getMovieDetail_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getMovieDetailUseCase.getMovieDetail(movieId, usRegion) } returns
      successFlow(mockMediaDetail)

    testViewModelFlow(
      runBlock = { viewModel.getMovieDetail(movieId, usRegion) },
      liveData = viewModel.detailMedia,
      expectedSuccess = mockMediaDetail,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieDetail(movieId, usRegion) } },
    )
  }

  @Test
  fun getMovieDetail_whenUnsuccessful_emitsError() = runTest {
    coEvery { getMovieDetailUseCase.getMovieDetail(movieId, usRegion) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieDetail(movieId, usRegion) },
      liveData = viewModel.detailMedia,
      expectError = errorMessage,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieDetail(movieId, usRegion) } }
    )
  }

  @Test
  fun getMovieDetail_whenLoading_doesNothing() = runTest {
    coEvery { getMovieDetailUseCase.getMovieDetail(movieId, usRegion) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieDetail(movieId, usRegion) },
      liveData = viewModel.detailMedia,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieDetail(movieId, usRegion) } }
    )
  }

  @Test
  fun getMovieVideoLinks_whenSuccessful_emitsSuccess() {
    coEvery { getMovieDetailUseCase.getMovieVideoLinks(movieId) } returns
      successFlow(mockLinkVideo)

    testViewModelFlow(
      runBlock = { viewModel.getMovieVideoLink(movieId) },
      liveData = viewModel.linkVideo,
      expectedSuccess = mockLinkVideo,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieVideoLinks(movieId) } }
    )
  }

  @Test
  fun getMovieVideoLinks_whenUnsuccessful_emitsError() {
    coEvery { getMovieDetailUseCase.getMovieVideoLinks(movieId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieVideoLink(movieId) },
      liveData = viewModel.linkVideo,
      expectError = errorMessage,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieVideoLinks(movieId) } }
    )
  }

  @Test
  fun getMovieVideoLinks_whenLoading_doesNothing() {
    coEvery { getMovieDetailUseCase.getMovieVideoLinks(movieId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieVideoLink(movieId) },
      liveData = viewModel.linkVideo,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieVideoLinks(movieId) } }
    )
  }

  @Test
  fun getMovieCredits_whenSuccessful_emitsSuccess() {
    coEvery { getMovieDetailUseCase.getMovieCredits(movieId) } returns
      successFlow(mockMediaCredits)

    testViewModelFlow(
      runBlock = { viewModel.getMovieCredits(movieId) },
      liveData = viewModel.mediaCredits,
      expectedSuccess = mockMediaCredits,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieCredits(movieId) } }
    )
  }

  @Test
  fun getMovieCredits_whenUnsuccessful_emitsError() {
    coEvery { getMovieDetailUseCase.getMovieCredits(movieId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieCredits(movieId) },
      liveData = viewModel.mediaCredits,
      expectError = errorMessage,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieCredits(movieId) } }
    )
  }

  @Test
  fun getMovieCredits_whenLoading_doesNothing() {
    coEvery { getMovieDetailUseCase.getMovieCredits(movieId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieCredits(movieId) },
      liveData = viewModel.mediaCredits,
      verifyBlock = { coVerify { getMovieDetailUseCase.getMovieCredits(movieId) } }
    )
  }

  @Test
  fun getMovieState_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getMovieStateUseCase.getMovieState(sessionId, movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlow(
      runBlock = { viewModel.getMovieState(sessionId, movieId) },
      liveData = viewModel.itemState,
      expectedSuccess = mockMediaStated,
      verifyBlock = { coVerify { getMovieStateUseCase.getMovieState(sessionId, movieId) } },
    )
  }

  @Test
  fun getMovieState_whenUnsuccessful_emitsError() = runTest {
    coEvery { getMovieStateUseCase.getMovieState(sessionId, movieId) } returns errorFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieState(sessionId, movieId) },
      liveData = viewModel.itemState,
      expectError = errorMessage,
      verifyBlock = { coVerify { getMovieStateUseCase.getMovieState(sessionId, movieId) } }
    )
  }

  @Test
  fun getMovieState_whenLoading_doesNothing() = runTest {
    coEvery { getMovieStateUseCase.getMovieState(sessionId, movieId) } returns loadingFlow

    testViewModelFlow(
      runBlock = { viewModel.getMovieState(sessionId, movieId) },
      liveData = viewModel.itemState,
      verifyBlock = { coVerify { getMovieStateUseCase.getMovieState(sessionId, movieId) } }
    )
  }

  @Test
  fun getMovieWatchProviders_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) } returns
      successFlow(mockWatchProvider)

    testSealedUiStateFlow(
      runBlock = { viewModel.getMovieWatchProviders(usRegion, movieId) },
      liveData = viewModel.watchProvidersUiState,
      expectedState = mockWatchProviderState,
      verifyBlock = {
        coVerify { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) }
      }
    )
  }

  @Test
  fun getMovieWatchProviders_whenUnsuccessful_emitsError() = runTest {
    coEvery { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) } returns
      errorFlow

    testSealedUiStateFlow(
      runBlock = { viewModel.getMovieWatchProviders(usRegion, movieId) },
      liveData = viewModel.watchProvidersUiState,
      expectedState = WatchProvidersUiState.Error(errorMessage),
      verifyBlock = {
        coVerify { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) }
      }
    )
  }

  @Test
  fun getMovieWatchProviders_whenLoading_emitsLoading() = runTest {
    coEvery { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) } returns
      loadingFlow

    testSealedUiStateFlow(
      runBlock = { viewModel.getMovieWatchProviders(usRegion, movieId) },
      liveData = viewModel.watchProvidersUiState,
      expectedState = WatchProvidersUiState.Loading,
      verifyBlock = {
        coVerify { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) }
      }
    )
  }

  @Test
  fun getMovieRecommendation_whenSuccessful_emitsPagingData() {
    coEvery { getMovieDetailUseCase.getMovieRecommendationPagingData(movieId) } returns
      flowOf(PagingData.from(listOf(mockMediaItem)))

    testPagingLiveData(
      liveData = viewModel.recommendation,
      runBlock = { viewModel.getMovieRecommendation(movieId) },
      itemAssertions = { snapshot ->
        assertThat(snapshot).isNotEmpty()
      }
    )
  }

  @Test
  fun getMovieWatchProviders_withNullFields_triggersOrEmptyBranches() = runTest {
    coEvery { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) } returns
      successFlow(nullProvider)

    viewModel.getMovieWatchProviders(usRegion, movieId)
    advanceUntilIdle()

    assertThat(viewModel.watchProvidersUiState.value).isEqualTo(
      WatchProvidersUiState
        .Success(
          emptyList(), emptyList(), emptyList(), emptyList(), emptyList()
        )
    )
  }

  @Test
  fun getMovieWatchProviders_withNonNullFields_skipsOrEmptyBranches() = runTest {
    coEvery { getMovieDetailUseCase.getMovieWatchProviders(usRegion, movieId) } returns
      successFlow(fullProvider)

    viewModel.getMovieWatchProviders(usRegion, movieId)
    advanceUntilIdle()

    assertThat(viewModel.watchProvidersUiState.value)
      .isInstanceOf(WatchProvidersUiState.Success::class.java)
  }

  @Test
  fun executeUseCase_whenNoOnSuccessProvided_shouldStillComplete() = runTest {
    val flow = flowOf(Outcome.Success(Unit))

    viewModel.executeUseCase(
      flowProvider = { flow }
    )

    advanceUntilIdle()
    assertThat(viewModel.loadingState.value).isNull()
  }
}
