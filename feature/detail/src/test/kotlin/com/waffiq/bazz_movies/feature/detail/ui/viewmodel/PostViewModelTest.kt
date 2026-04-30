package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.feature.detail.domain.model.UpdateMediaStateResult
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.SESSION_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelAddFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelAddWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelDeleteFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelDeleteWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.PostTestHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostViewModelTest :
  BaseMediaDetailViewModelTest(),
  PostTestHelper {

  @Before
  override fun setup() {
    super.setup()
    every { mockUserPrefUseCase.getUserToken() } returns flowOf(SESSION_ID)
  }

  private val postFavoriteMovieData = FavoriteParams(
    mediaType = MOVIE_MEDIA_TYPE,
    mediaId = movieId,
    favorite = true,
  )
  private val postFavoriteDeleteData = postFavoriteMovieData.copy(favorite = false)
  private val postWatchlistMovieData = WatchlistParams(
    mediaType = MOVIE_MEDIA_TYPE,
    mediaId = movieId,
    watchlist = true,
  )
  private val postWatchlistDeleteData = postWatchlistMovieData.copy(watchlist = false)
  private val postFavoriteTvData = FavoriteParams(
    mediaType = TV_MEDIA_TYPE,
    mediaId = tvId,
    favorite = true,
  )
  private val postWatchlistTvData = WatchlistParams(
    mediaType = TV_MEDIA_TYPE,
    mediaId = tvId,
    watchlist = true,
  )

  private fun expectedFailed(isDelete: Boolean, isFavorite: Boolean) =
    UpdateMediaStateResult(
      isSuccess = false,
      isDelete = isDelete,
      isFavorite = isFavorite,

    )

  @Test
  fun postFavorite_withMovieWhenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteMovieData) } returns
        flowSuccessWithLoading(mockPostFavoriteWatchlist)
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns
        successFlow(mockMediaStated)

      testViewModelState(
        runBlock = { viewModel.postFavorite(postFavoriteMovieData) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddFavoriteStateSuccess),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteMovieData) }
        },
      )
    }

  @Test
  fun postFavorite_withTvWhenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteTvData) } returns
        flowSuccessWithLoading(mockPostFavoriteWatchlist)
      coEvery { mockMediaStateUseCase.getTvStateWithUser(tvId) } returns
        successFlow(mockMediaStated)

      testViewModelState(
        runBlock = { viewModel.postFavorite(postFavoriteTvData) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddFavoriteStateSuccess),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteTvData) }
        },
      )
    }

  @Test
  fun postFavorite_performDelete_emitsSuccess() =
    runTest {
      coEvery { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteDeleteData) } returns
        flowSuccessWithLoading(mockPostFavoriteWatchlist)
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns
        successFlow(mockMediaStated)

      testViewModelState(
        runBlock = { viewModel.postFavorite(postFavoriteDeleteData) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteFavoriteStateSuccess),
        expectedLoadingStates = listOf(true, false),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteDeleteData) }
        },
      )
    }

  @Test
  fun postFavorite_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteMovieData) } returns
        flowFailedWithLoading

      testViewModelState(
        runBlock = { viewModel.postFavorite(postFavoriteMovieData) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        expectedStates = listOf(expectedFailed(isDelete = false, isFavorite = true)),
        expectedLoadingStates = listOf(true, false),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteMovieData) }
        },
      )
    }

  @Test
  fun postFavorite_whenUnsuccessfulDelete_emitsError() =
    runTest {
      coEvery { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteDeleteData) } returns
        flowFailedWithLoading

      testViewModelState(
        runBlock = { viewModel.postFavorite(postFavoriteDeleteData) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        expectedStates = listOf(expectedFailed(isDelete = true, isFavorite = true)),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteDeleteData) }
        },
      )
    }

  @Test
  fun postFavorite_whenLoading_doesNothing() =
    runTest {
      coEvery { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteMovieData) } returns
        loadingFlow

      testViewModelState(
        runBlock = { viewModel.postFavorite(postFavoriteMovieData) },
        stateSelector = { it.mediaStateResult },
        expectedLoadingStates = listOf(true),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postFavoriteWithAuth(postFavoriteMovieData) }
        },
      )
    }

  @Test
  fun postWatchlist_withMovieWhenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistMovieData) } returns
        flowSuccessWithLoading(mockPostFavoriteWatchlist)
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns
        successFlow(mockMediaStated)

      testViewModelState(
        runBlock = { viewModel.postWatchlist(postWatchlistMovieData) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddWatchlistStateSuccess),
        expectedLoadingStates = listOf(true, false),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistMovieData) }
        },
      )
    }

  @Test
  fun postWatchlist_withTvWhenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistTvData) } returns
        flowSuccessWithLoading(mockPostFavoriteWatchlist)
      coEvery { mockMediaStateUseCase.getTvStateWithUser(tvId) } returns
        successFlow(mockMediaStated)

      testViewModelState(
        runBlock = { viewModel.postWatchlist(postWatchlistTvData) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddWatchlistStateSuccess),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistTvData) }
        },
      )
    }

  @Test
  fun postWatchlist_whenDelete_emitsSuccess() =
    runTest {
      coEvery { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistDeleteData) } returns
        flowSuccessWithLoading(mockPostFavoriteWatchlist)
      coEvery { mockMediaStateUseCase.getMovieStateWithUser(movieId) } returns
        successFlow(mockMediaStated)

      testViewModelState(
        runBlock = { viewModel.postWatchlist(postWatchlistDeleteData) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteWatchlistStateSuccess),
        expectedLoadingStates = listOf(true, false),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistDeleteData) }
        },
      )
    }

  @Test
  fun postWatchlist_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistMovieData) } returns
        flowFailedWithLoading

      testViewModelState(
        runBlock = { viewModel.postWatchlist(postWatchlistMovieData) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        expectedStates = listOf(expectedFailed(isDelete = false, isFavorite = false)),
        expectedLoadingStates = listOf(true, false),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistMovieData) }
        },
      )
    }

  @Test
  fun postWatchlist_whenUnsuccessfulDelete_emitsError() =
    runTest {
      coEvery { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistDeleteData) } returns
        flowFailedWithLoading

      testViewModelState(
        runBlock = { viewModel.postWatchlist(postWatchlistDeleteData) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        expectedStates = listOf(expectedFailed(isDelete = true, isFavorite = false)),
        expectedLoadingStates = listOf(true, false),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistDeleteData) }
        },
      )
    }

  @Test
  fun postWatchlist_whenLoading_doesNothing() =
    runTest {
      coEvery { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistMovieData) } returns
        loadingFlow

      testViewModelState(
        runBlock = { viewModel.postWatchlist(postWatchlistMovieData) },
        stateSelector = { it.mediaStateResult },
        expectedLoadingStates = listOf(true),
        verifyBlock = {
          coVerify { mockPostActionUseCase.postWatchlistWithAuth(postWatchlistMovieData) }
        },
      )
    }
}
