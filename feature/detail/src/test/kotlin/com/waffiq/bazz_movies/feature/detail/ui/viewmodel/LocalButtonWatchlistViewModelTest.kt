package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelAddWatchlistStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelDeleteWatchlistStateSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalButtonWatchlistViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun updateToWatchlistDB_whenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = true, isWatchlist = false)
      stubUpdateSuccess()

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddWatchlistStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) } },
      )
    }

  @Test
  fun updateToWatchlistDB_whenUnsuccessful_emitsError() =
    runTest {
      stubState(isFavorite = true, isWatchlist = false)
      coEvery { mockLocalDatabaseUseCase.update(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) } },
      )
    }

  @Test
  fun insertToDB_whenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = false, isWatchlist = false)
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns successDbResult(1)

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddWatchlistStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } },
      )
    }

  @Test
  fun insertToDB_whenUnsuccessful_emitsError() =
    runTest {
      stubState(isFavorite = false, isWatchlist = false)
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } },
      )
    }

  @Test
  fun updateToRemoveFromWatchlistDB_whenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = true, isWatchlist = true)
      stubUpdateSuccess()

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteWatchlistStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) } },
      )
    }

  @Test
  fun updateToRemoveFromWatchlistDB_whenUnsuccessful_emitsError() =
    runTest {
      stubState(isFavorite = true, isWatchlist = true)
      coEvery { mockLocalDatabaseUseCase.update(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) } },
      )
    }

  @Test
  fun delFromFavoriteDB_whenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = false, isWatchlist = true)
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } returns successDbResult(1)

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteWatchlistStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } },
      )
    }

  @Test
  fun delFromFavoriteDB_whenUnsuccessful_emitsError() =
    runTest {
      stubState(isFavorite = false, isWatchlist = true)
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.handleBtnWatchlist(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } },
      )
    }
}
