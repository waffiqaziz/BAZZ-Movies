package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelAddFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelAddWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelDeleteFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelDeleteWatchlistStateSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.test.assertEquals

class LocalDatabaseViewModelTest : BaseMediaDetailViewModelTest() {

  // region BUTTON FAVORITE
  @Test
  fun updateToFavoriteDB_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.updateFavoriteItemDB(false, any<Favorite>()) } returns
        successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = false,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddFavoriteStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateFavoriteItemDB(false, any<Favorite>()) }
        },
      )
    }

  @Test
  fun updateToFavoriteDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery {
        mockLocalDatabaseUseCase.updateFavoriteItemDB(
          false,
          any<Favorite>(),
        )
      } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = false,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateFavoriteItemDB(false, any<Favorite>()) }
        },
      )
    }

  @Test
  fun insertToDB_withBtnFavoriteWhenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = false,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddFavoriteStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) }
        },
      )
    }

  @Test
  fun insertToDB_withBtnFavoriteWhenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = false,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } },
      )
    }

  @Test
  fun updateToRemoveFromFavoriteDB_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.updateFavoriteItemDB(true, any<Favorite>()) } returns
        successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = true,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteFavoriteStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateFavoriteItemDB(true, any<Favorite>()) }
        },
      )
    }

  @Test
  fun updateToRemoveFromFavoriteDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery {
        mockLocalDatabaseUseCase.updateFavoriteItemDB(
          true,
          any<Favorite>(),
        )
      } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = true,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateFavoriteItemDB(true, any<Favorite>()) }
        },
      )
    }

  @Test
  fun delFromFavoriteDB_withBtnFavoriteWhenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = true,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteFavoriteStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) }
        },
      )
    }

  @Test
  fun delFromFavoriteDB_withBtnFavoriteWhenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnFavorite(
            favorite = true,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) } },
      )
    }
  // endregion BUTTON FAVORITE

  // region BUTTON WATCHLIST
  @Test
  fun updateToWatchlistDB_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.updateWatchlistItemDB(false, any<Favorite>()) } returns
        successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = true,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddWatchlistStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateWatchlistItemDB(false, any<Favorite>()) }
        },
      )
    }

  @Test
  fun updateToWatchlistDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery {
        mockLocalDatabaseUseCase.updateWatchlistItemDB(
          false,
          any<Favorite>(),
        )
      } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = true,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateWatchlistItemDB(false, any<Favorite>()) }
        },
      )
    }

  @Test
  fun insertToDB_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns
        successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = false,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddWatchlistStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) }
        },
      )
    }

  @Test
  fun insertToDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = false,
            watchlist = false,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) }
        },
      )
    }

  @Test
  fun updateToRemoveFromWatchlistDB_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.updateWatchlistItemDB(true, any<Favorite>()) } returns
        successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = true,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteWatchlistStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateWatchlistItemDB(true, any<Favorite>()) }
        },
      )
    }

  @Test
  fun updateToRemoveFromWatchlistDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery {
        mockLocalDatabaseUseCase.updateWatchlistItemDB(
          true,
          any<Favorite>(),
        )
      } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = true,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.updateWatchlistItemDB(true, any<Favorite>()) }
        },
      )
    }

  @Test
  fun delFromFavoriteDB_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns
        successDbResult(1)

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = false,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteWatchlistStateSuccess),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) }
        },
      )
    }

  @Test
  fun delFromFavoriteDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = {
          viewModel.handleBtnWatchlist(
            favorite = false,
            watchlist = true,
            data = dataMediaItem,
          )
        },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.deleteFromDB(any<Favorite>()) }
        },
      )
    }
  // endregion BUTTON WATCHLIST

  // region FAVORITE & WATCHLIST STATE
  @Test
  fun isFavoriteDB_whenSuccessfulAndIsFavorite_emitsTrueValue() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) } returns
        successDbResult(true)

      testViewModelState(
        runBlock = { viewModel.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) },
        stateSelector = { it.isFavorite },
        expectedStates = listOf(false, true),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) }
        },
      )
    }

  @Test
  fun isFavoriteDB_whenSuccessfulAndNotFavorite_emitsNullValue() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) } returns
        successDbResult(false)

      testViewModelState(
        runBlock = { viewModel.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) },
        stateSelector = { it.isFavorite },
        expectedStates = listOf(false),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) }
        },
      )
    }

  @Test
  fun isFavoriteDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery {
        mockLocalDatabaseUseCase.isFavoriteDB(
          movieId,
          MOVIE_MEDIA_TYPE,
        )
      } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) },
        stateSelector = { it.isFavorite },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.isFavoriteDB(movieId, MOVIE_MEDIA_TYPE) }
        },
      )
    }

  @Test
  fun isWatchlistDB_whenSuccessfulAndIsFavorite_emitsTrueValue() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) } returns
        successDbResult(true)

      testViewModelState(
        runBlock = { viewModel.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) },
        stateSelector = { it.isWatchlist },
        expectedStates = listOf(false, true),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) }
        },
      )
    }

  @Test
  fun isWatchlistDB_whenSuccessfulAndNotFavorite_emitsNullValue() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) } returns
        successDbResult(false)

      testViewModelState(
        runBlock = { viewModel.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) },
        stateSelector = { it.isWatchlist },
        expectedStates = listOf(false),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) }
        },
      )
    }

  @Test
  fun isWatchlistDB_whenUnsuccessful_emitsError() =
    runTest {
      coEvery {
        mockLocalDatabaseUseCase.isWatchlistDB(
          movieId,
          MOVIE_MEDIA_TYPE,
        )
      } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) },
        stateSelector = { it.isWatchlist },
        expectedErrors = listOf(errorMessage),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.isWatchlistDB(movieId, MOVIE_MEDIA_TYPE) }
        },
      )
    }
  // endregion FAVORITE & WATCHLIST STATE

  @Test
  fun consumeMediaStateResult_whenCalled_shouldUpdateTheState() =
    runTest {
      // setup ui state
      coEvery { mockLocalDatabaseUseCase.updateFavoriteItemDB(false, any<Favorite>()) } returns
        successDbResult(1)
      viewModel.handleBtnFavorite(favorite = false, watchlist = true, data = dataMediaItem)
      advanceUntilIdle()

      // should return true
      assertNotNull(viewModel.uiState.value.mediaStateResult)
      assertEquals(viewModel.uiState.value.mediaStateResult?.isFavorite ?: false, true)

      // call
      viewModel.consumeMediaStateResult()
      advanceUntilIdle()

      // should update to null
      assertNull(viewModel.uiState.value.mediaStateResult)
    }
}
