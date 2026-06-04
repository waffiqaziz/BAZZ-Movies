package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelAddFavoriteStateSuccess
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.postModelDeleteFavoriteStateSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalButtonFavoriteViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun updateDB_whenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = false, isWatchlist = true)
      coEvery { mockLocalDatabaseUseCase.update(any<Favorite>()) } returns
        successDbResult(Unit)

      testViewModelState(
        runBlock = { viewModel.handleBtnFavorite(dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddFavoriteStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) } },
      )
    }

  @Test
  fun insertToDB_withBtnFavoriteWhenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = false, isWatchlist = false)
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns successDbResult(1)

      testViewModelState(
        runBlock = { viewModel.handleBtnFavorite(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelAddFavoriteStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } },
      )
    }

  @Test
  fun insertToDB_withBtnFavoriteWhenUnsuccessful_emitsError() =
    runTest {
      stubState(isFavorite = false, isWatchlist = false)
      coEvery { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.handleBtnFavorite(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.insertToDB(any<Favorite>()) } },
      )
    }

  @Test
  fun updateToRemoveFromFavoriteDB_whenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = true, isWatchlist = true)
      coEvery { mockLocalDatabaseUseCase.update(any<Favorite>()) } returns
        successDbResult(Unit)

      testViewModelState(
        runBlock = { viewModel.handleBtnFavorite(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteFavoriteStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) } },
      )
    }

  @Test
  fun updateToRemoveFromFavoriteDB_whenUnsuccessful_emitsError() =
    runTest {
      stubState(isFavorite = true, isWatchlist = true)
      coEvery { mockLocalDatabaseUseCase.update(any<Favorite>()) } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.handleBtnFavorite(data = dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) } },
      )
    }

  @Test
  fun delFromFavoriteDB_withBtnFavoriteWhenSuccessful_emitsSuccess() =
    runTest {
      stubState(isFavorite = true, isWatchlist = false)
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } returns successDbResult(1)

      testViewModelState(
        runBlock = { viewModel.handleBtnFavorite(dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedStates = listOf(postModelDeleteFavoriteStateSuccess),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } },
      )
    }

  @Test
  fun delFromFavoriteDB_withBtnFavoriteWhenUnsuccessful_emitsError() =
    runTest {
      stubState(isFavorite = true, isWatchlist = false)
      coEvery { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } returns errorDbResult

      testViewModelState(
        runBlock = { viewModel.handleBtnFavorite(dataMediaItem) },
        stateSelector = { it.mediaStateResult },
        expectedErrors = listOf(errorMessage),
        verifyBlock = { coVerify { mockLocalDatabaseUseCase.deleteFromDB(any(), any()) } },
      )
    }
}
