package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelAddFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelAddWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelDeleteFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelDeleteWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.PostTestHelper
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PostViewModelTest : BaseMediaDetailViewModelTest(), PostTestHelper {

  private val userId = 12345678
  private val postFavoriteMovieData = FavoriteModel(
    mediaType = MOVIE_MEDIA_TYPE,
    mediaId = movieId,
    favorite = true
  )
  private val postFavoriteDeleteData = postFavoriteMovieData.copy(favorite = false)
  private val postWatchlistMovieData = WatchlistModel(
    mediaType = MOVIE_MEDIA_TYPE,
    mediaId = movieId,
    watchlist = true
  )
  private val postWatchlistDeleteData = postWatchlistMovieData.copy(watchlist = false)
  private val postFavoriteTvData = FavoriteModel(
    mediaType = TV_MEDIA_TYPE,
    mediaId = tvId,
    favorite = true
  )
  private val postWatchlistTvData = WatchlistModel(
    mediaType = TV_MEDIA_TYPE,
    mediaId = tvId,
    watchlist = true
  )

  private fun expectedFailed(isDelete: Boolean, isFavorite: Boolean) = Event(
    PostModelState(
      isSuccess = false,
      isDelete = isDelete,
      isFavorite = isFavorite
    )
  )

  @Test
  fun postFavorite_withMovieWhenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postFavorite(sessionId, postFavoriteMovieData, userId) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(sessionId, postFavoriteMovieData, userId) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddFavoriteStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postFavorite(sessionId, postFavoriteMovieData, userId) }
      },
    )
  }

  @Test
  fun postFavorite_withTvWhenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postFavorite(sessionId, postFavoriteTvData, userId) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(sessionId, postFavoriteTvData, userId) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddFavoriteStateSuccess),
      verifyBlock = {
        coVerify { postMethodUseCase.postFavorite(sessionId, postFavoriteTvData, userId) }
      },
    )
  }

  @Test
  fun postFavorite_performDelete_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postFavorite(sessionId, postFavoriteDeleteData, userId) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(sessionId, postFavoriteDeleteData, userId) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteFavoriteStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postFavorite(sessionId, postFavoriteDeleteData, userId) }
      },
    )
  }

  @Test
  fun postFavorite_whenUnsuccessful_emitsError() = runTest {
    coEvery { postMethodUseCase.postFavorite(sessionId, postFavoriteMovieData, userId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(sessionId, postFavoriteMovieData, userId) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = false, isFavorite = true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postFavorite(sessionId, postFavoriteMovieData, userId) }
      }
    )
  }

  @Test
  fun postFavorite_whenUnsuccessfulDelete_emitsError() = runTest {
    coEvery { postMethodUseCase.postFavorite(sessionId, postFavoriteDeleteData, userId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(sessionId, postFavoriteDeleteData, userId) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = true, isFavorite = true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postFavorite(sessionId, postFavoriteDeleteData, userId) }
      }
    )
  }

  @Test
  fun postFavorite_whenLoading_doesNothing() = runTest {
    coEvery { postMethodUseCase.postFavorite(sessionId, postFavoriteMovieData, userId) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(sessionId, postFavoriteMovieData, userId) },
      liveData = viewModel.postModelState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postFavorite(sessionId, postFavoriteMovieData, userId) }
      }
    )
  }

  @Test
  fun postWatchlist_withMovieWhenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postWatchlist(sessionId, postWatchlistMovieData, userId) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(sessionId, postWatchlistMovieData, userId) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddWatchlistStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postWatchlist(sessionId, postWatchlistMovieData, userId) }
      },
    )
  }

  @Test
  fun postWatchlist_withTvWhenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postWatchlist(sessionId, postWatchlistTvData, userId) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(sessionId, postWatchlistTvData, userId) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddWatchlistStateSuccess),
      verifyBlock = {
        coVerify { postMethodUseCase.postWatchlist(sessionId, postWatchlistTvData, userId) }
      },
    )
  }

  @Test
  fun postWatchlist_whenDelete_emitsSuccess() = runTest {
    coEvery { postMethodUseCase.postWatchlist(sessionId, postWatchlistDeleteData, userId) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(sessionId, postWatchlistDeleteData, userId) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteWatchlistStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postWatchlist(sessionId, postWatchlistDeleteData, userId) }
      },
    )
  }

  @Test
  fun postWatchlist_whenUnsuccessful_emitsError() = runTest {
    coEvery { postMethodUseCase.postWatchlist(sessionId, postWatchlistMovieData, userId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(sessionId, postWatchlistMovieData, userId) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = false, isFavorite = false),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postWatchlist(sessionId, postWatchlistMovieData, userId) }
      }
    )
  }

  @Test
  fun postWatchlist_whenUnsuccessfulDelete_emitsError() = runTest {
    coEvery { postMethodUseCase.postWatchlist(sessionId, postWatchlistDeleteData, userId) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(sessionId, postWatchlistDeleteData, userId) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = true, isFavorite = false),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postWatchlist(sessionId, postWatchlistDeleteData, userId) }
      }
    )
  }

  @Test
  fun postWatchlist_whenLoading_doesNothing() = runTest {
    coEvery { postMethodUseCase.postWatchlist(sessionId, postWatchlistMovieData, userId) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(sessionId, postWatchlistMovieData, userId) },
      liveData = viewModel.postModelState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodUseCase.postWatchlist(sessionId, postWatchlistMovieData, userId) }
      }
    )
  }
}
