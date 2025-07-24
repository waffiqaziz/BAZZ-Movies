package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelAddFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelAddWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelDeleteFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.postModelDeleteWatchlistStateSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalDatabaseViewModelTest : BaseMediaDetailViewModelTest() {

  // region BUTTON FAVORITE
  @Test
  fun updateToFavoriteDB_whenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.updateFavoriteItemDB(false, any<Favorite>()) } returns
      successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = false,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddFavoriteStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateFavoriteItemDB(false, any<Favorite>()) }
      },
    )
  }

  @Test
  fun updateToFavoriteDB_whenUnsuccessful_emitsError() = runTest {
    coEvery {
      localDatabaseUseCase.updateFavoriteItemDB(
        false,
        any<Favorite>()
      )
    } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = false,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateFavoriteItemDB(false, any<Favorite>()) }
      }
    )
  }

  @Test
  fun insertToDB_withBtnFavoriteWhenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.insertToDB(any<Favorite>()) } returns successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = false,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddFavoriteStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.insertToDB(any<Favorite>()) }
      },
    )
  }

  @Test
  fun insertToDB_withBtnFavoriteWhenUnsuccessful_emitsError() = runTest {
    coEvery { localDatabaseUseCase.insertToDB(any<Favorite>()) } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = false,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = { coVerify { localDatabaseUseCase.insertToDB(any<Favorite>()) } }
    )
  }

  @Test
  fun updateToRemoveFromFavoriteDB_whenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.updateFavoriteItemDB(true, any<Favorite>()) } returns
      successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = true,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteFavoriteStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateFavoriteItemDB(true, any<Favorite>()) }
      },
    )
  }

  @Test
  fun updateToRemoveFromFavoriteDB_whenUnsuccessful_emitsError() = runTest {
    coEvery {
      localDatabaseUseCase.updateFavoriteItemDB(
        true,
        any<Favorite>()
      )
    } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = true,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateFavoriteItemDB(true, any<Favorite>()) }
      }
    )
  }

  @Test
  fun delFromFavoriteDB_withBtnFavoriteWhenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = true,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteFavoriteStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.deleteFromDB(any<Favorite>()) }
      },
    )
  }

  @Test
  fun delFromFavoriteDB_withBtnFavoriteWhenUnsuccessful_emitsError() = runTest {
    coEvery { localDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnFavorite(
          favorite = true,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = { coVerify { localDatabaseUseCase.deleteFromDB(any<Favorite>()) } }
    )
  }
  // endregion BUTTON FAVORITE

  // region BUTTON WATCHLIST
  @Test
  fun updateToWatchlistDB_whenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.updateWatchlistItemDB(false, any<Favorite>()) } returns
      successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = true,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddWatchlistStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateWatchlistItemDB(false, any<Favorite>()) }
      },
    )
  }

  @Test
  fun updateToWatchlistDB_whenUnsuccessful_emitsError() = runTest {
    coEvery {
      localDatabaseUseCase.updateWatchlistItemDB(
        false,
        any<Favorite>()
      )
    } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = true,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateWatchlistItemDB(false, any<Favorite>()) }
      }
    )
  }

  @Test
  fun insertToDB_whenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.insertToDB(any<Favorite>()) } returns
      successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = false,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelAddWatchlistStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.insertToDB(any<Favorite>()) }
      },
    )
  }

  @Test
  fun insertToDB_whenUnsuccessful_emitsError() = runTest {
    coEvery { localDatabaseUseCase.insertToDB(any<Favorite>()) } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = false,
          watchlist = false,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.insertToDB(any<Favorite>()) }
      }
    )
  }

  @Test
  fun updateToRemoveFromWatchlistDB_whenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.updateWatchlistItemDB(true, any<Favorite>()) } returns
      successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = true,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteWatchlistStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateWatchlistItemDB(true, any<Favorite>()) }
      },
    )
  }

  @Test
  fun updateToRemoveFromWatchlistDB_whenUnsuccessful_emitsError() = runTest {
    coEvery {
      localDatabaseUseCase.updateWatchlistItemDB(
        true,
        any<Favorite>()
      )
    } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = true,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.updateWatchlistItemDB(true, any<Favorite>()) }
      }
    )
  }

  @Test
  fun delFromFavoriteDB_whenSuccessful_emitsSuccess() = runTest {
    coEvery { localDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns
      successDbResult(1)

    testViewModelFlowEvent(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = false,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectedSuccess = Event(postModelDeleteWatchlistStateSuccess),
      verifyBlock = {
        coVerify { localDatabaseUseCase.deleteFromDB(any<Favorite>()) }
      },
    )
  }

  @Test
  fun delFromFavoriteDB_whenUnsuccessful_emitsError() = runTest {
    coEvery { localDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns errorDbResult

    testViewModelFlow(
      runBlock = {
        viewModel.handleBtnWatchlist(
          favorite = false,
          watchlist = true,
          data = dataMediaItem
        )
      },
      liveData = viewModel.postModelState,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.deleteFromDB(any<Favorite>()) }
      }
    )
  }
  // endregion BUTTON WATCHLIST

  @Test
  fun isFavoriteDB_whenSuccessfulAndIsFavorite_emitsTrueValue() = runTest {
    coEvery { localDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) } returns
      successDbResult(true)

    testViewModelFlowEvent(
      runBlock = { viewModel.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) },
      liveData = viewModel.isFavorite,
      expectedSuccess = true,
      verifyBlock = {
        coVerify { localDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) }
      },
    )
  }

  @Test
  fun isFavoriteDB_whenSuccessfulAndNotFavorite_emitsNullValue() = runTest {
    coEvery { localDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) } returns
      successDbResult(false)

    testViewModelFlowEvent(
      runBlock = { viewModel.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) },
      liveData = viewModel.isFavorite,
      expectedSuccess = null,
      verifyBlock = {
        coVerify { localDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) }
      },
    )
  }

  @Test
  fun isFavoriteDB_whenUnsuccessful_emitsError() = runTest {
    coEvery { localDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) } returns errorDbResult

    testViewModelFlow(
      runBlock = { viewModel.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) },
      liveData = viewModel.isFavorite,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) }
      }
    )
  }

  @Test
  fun isWatchlistDB_whenSuccessfulAndIsFavorite_emitsTrueValue() = runTest {
    coEvery { localDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) } returns
      successDbResult(true)

    testViewModelFlowEvent(
      runBlock = { viewModel.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) },
      liveData = viewModel.isWatchlist,
      expectedSuccess = true,
      verifyBlock = {
        coVerify { localDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) }
      },
    )
  }

  @Test
  fun isWatchlistDB_whenSuccessfulAndNotFavorite_emitsNullValue() = runTest {
    coEvery { localDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) } returns
      successDbResult(false)

    testViewModelFlowEvent(
      runBlock = { viewModel.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) },
      liveData = viewModel.isWatchlist,
      expectedSuccess = null,
      verifyBlock = {
        coVerify { localDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) }
      },
    )
  }

  @Test
  fun isWatchlistDB_whenUnsuccessful_emitsError() = runTest {
    coEvery { localDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) } returns errorDbResult

    testViewModelFlow(
      runBlock = { viewModel.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) },
      liveData = viewModel.isWatchlist,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { localDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) }
      }
    )
  }
}
