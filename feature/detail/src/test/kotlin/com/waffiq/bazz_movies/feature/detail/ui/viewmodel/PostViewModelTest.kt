package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.feature.detail.domain.model.PostModelState
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.SESSION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelAddFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelAddWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelDeleteFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelDeleteWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.PostTestHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostViewModelTest : BaseMediaDetailViewModelTest(), PostTestHelper {

  @Before
  override fun setup() {
    super.setup()
    every { mockUserPrefUseCase.getUserToken() } returns flowOf(SESSION_ID)
  }

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
    coEvery { postMethodWithUserUseCase.postFavorite(postFavoriteMovieData) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(postFavoriteMovieData) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddFavoriteStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postFavorite(postFavoriteMovieData) }
      },
    )
  }

  @Test
  fun postFavorite_withTvWhenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodWithUserUseCase.postFavorite(postFavoriteTvData) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(postFavoriteTvData) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddFavoriteStateSuccess),
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postFavorite(postFavoriteTvData) }
      },
    )
  }

  @Test
  fun postFavorite_performDelete_emitsSuccess() = runTest {
    coEvery { postMethodWithUserUseCase.postFavorite(postFavoriteDeleteData) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(postFavoriteDeleteData) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteFavoriteStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postFavorite(postFavoriteDeleteData) }
      },
    )
  }

  @Test
  fun postFavorite_whenUnsuccessful_emitsError() = runTest {
    coEvery { postMethodWithUserUseCase.postFavorite(postFavoriteMovieData) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(postFavoriteMovieData) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = false, isFavorite = true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postFavorite(postFavoriteMovieData) }
      }
    )
  }

  @Test
  fun postFavorite_whenUnsuccessfulDelete_emitsError() = runTest {
    coEvery { postMethodWithUserUseCase.postFavorite(postFavoriteDeleteData) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(postFavoriteDeleteData) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = true, isFavorite = true),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postFavorite(postFavoriteDeleteData) }
      }
    )
  }

  @Test
  fun postFavorite_whenLoading_doesNothing() = runTest {
    coEvery { postMethodWithUserUseCase.postFavorite(postFavoriteMovieData) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postFavorite(postFavoriteMovieData) },
      liveData = viewModel.postModelState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postFavorite(postFavoriteMovieData) }
      }
    )
  }

  @Test
  fun postWatchlist_withMovieWhenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodWithUserUseCase.postWatchlist(postWatchlistMovieData) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(postWatchlistMovieData) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddWatchlistStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postWatchlist(postWatchlistMovieData) }
      },
    )
  }

  @Test
  fun postWatchlist_withTvWhenSuccessful_emitsSuccess() = runTest {
    coEvery { postMethodWithUserUseCase.postWatchlist(postWatchlistTvData) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getTvStateWithUser(tvId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(postWatchlistTvData) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddWatchlistStateSuccess),
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postWatchlist(postWatchlistTvData) }
      },
    )
  }

  @Test
  fun postWatchlist_whenDelete_emitsSuccess() = runTest {
    coEvery { postMethodWithUserUseCase.postWatchlist(postWatchlistDeleteData) } returns
      flowSuccessWithLoading(mockPostFavoriteWatchlist)
    coEvery { getMediaStateWithUserUseCase.getMovieStateWithUser(movieId) } returns
      successFlow(mockMediaStated)

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(postWatchlistDeleteData) },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteWatchlistStateSuccess),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postWatchlist(postWatchlistDeleteData) }
      },
    )
  }

  @Test
  fun postWatchlist_whenUnsuccessful_emitsError() = runTest {
    coEvery { postMethodWithUserUseCase.postWatchlist(postWatchlistMovieData) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(postWatchlistMovieData) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = false, isFavorite = false),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postWatchlist(postWatchlistMovieData) }
      }
    )
  }

  @Test
  fun postWatchlist_whenUnsuccessfulDelete_emitsError() = runTest {
    coEvery { postMethodWithUserUseCase.postWatchlist(postWatchlistDeleteData) } returns
      flowFailedWithLoading

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(postWatchlistDeleteData) },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      expectedSuccess = expectedFailed(isDelete = true, isFavorite = false),
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postWatchlist(postWatchlistDeleteData) }
      }
    )
  }

  @Test
  fun postWatchlist_whenLoading_doesNothing() = runTest {
    coEvery { postMethodWithUserUseCase.postWatchlist(postWatchlistMovieData) } returns
      loadingFlow

    testViewModelFlowEvent(
      runBlock = { viewModel.postWatchlist(postWatchlistMovieData) },
      liveData = viewModel.postModelState,
      checkLoading = true,
      verifyBlock = {
        coVerify { postMethodWithUserUseCase.postWatchlist(postWatchlistMovieData) }
      }
    )
  }
}
