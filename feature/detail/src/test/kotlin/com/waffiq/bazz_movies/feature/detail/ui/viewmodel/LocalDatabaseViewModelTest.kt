package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.dataMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.favoriteMovie
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.test.assertEquals

class LocalDatabaseViewModelTest : BaseMediaDetailViewModelTest() {
  @Test
  fun getByMedia_whenMediaNotFavorite_emitsCorrectly() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.getByMedia(movieId, MOVIE_MEDIA_TYPE) } returns
        favoriteMovie

      testViewModelState(
        runBlock = { viewModel.getByMedia(movieId, MOVIE_MEDIA_TYPE) },
        stateSelector = { it.isWatchlist },
        expectedStates = listOf(false),
        verifyBlock = {
          coVerify { mockLocalDatabaseUseCase.getByMedia(movieId, MOVIE_MEDIA_TYPE) }
        },
      )
    }

  @Test
  fun getByMedia_whenNotFound_doNothing() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.getByMedia(any(), any()) } returns null
      viewModel.getByMedia(1, "")
    }

  @Test
  fun getByMedia_whenLastUpdatedNotStalled_doNothing() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.getByMedia(any(), any()) } returns
        favoriteMovie
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(movieId) } returns
        successFlow(mockMediaDetail)

      viewModel.getByMedia(1, "")
      advanceUntilIdle()

      coVerify { mockLocalDatabaseUseCase.getByMedia(any(), any()) }
      coVerify(exactly = 0) { mockLocalDatabaseUseCase.update(any<Favorite>()) }
    }

  @Test
  fun getByMedia_whenSuccessful_runsCorrectly() =
    runTest {
      coEvery { mockGetOMDbDetailUseCase.getOMDbDetails(any()) } returns successFlow(mockOmdb)
      coEvery { mockLocalDatabaseUseCase.update(any()) } returns successDbResult(Unit)
      coEvery { mockLocalDatabaseUseCase.getByMedia(any(), any()) } returns
        favoriteMovie.copy(lastUpdated = 12324, isFavorite = true)
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(any()) } returns
        successFlow(mockMediaDetail)

      viewModel.getMovieDetail(movieId)
      advanceUntilIdle()

      viewModel.getByMedia(43, "movie")
      advanceUntilIdle()

      coVerify { mockLocalDatabaseUseCase.getByMedia(any(), any()) }
      coVerify { mockLocalDatabaseUseCase.update(any<Favorite>()) }
    }

  @Test
  fun getByMedia_whenDetailIsNull_doNoting() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.getByMedia(any(), any()) } returns
        favoriteMovie.copy(lastUpdated = 12324, isFavorite = true)
      // error when get detail (detail = null)
      coEvery { mockGetMediaDetailUseCase.getMovieDetailWithUserRegion(any()) } returns errorFlow

      viewModel.getMovieDetail(movieId)
      advanceUntilIdle()

      viewModel.getByMedia(14323, "movie")
      advanceUntilIdle()

      coVerify { mockLocalDatabaseUseCase.getByMedia(any(), any()) }
      coVerify(exactly = 0) { mockLocalDatabaseUseCase.update(any<Favorite>()) }
    }

  @Test
  fun refreshMedia_whenSuccessful_returnsCorrectly() =
    runTest {
      coEvery { mockRefreshMediaMetadataUseCase.refreshMedia(any(), any()) } just Runs

      viewModel.refreshMedia(221, "movie")
      advanceUntilIdle()

      coVerify { mockRefreshMediaMetadataUseCase.refreshMedia(any(), any()) }
    }

  @Test
  fun consumeMediaStateResult_whenCalled_shouldUpdateTheState() =
    runTest {
      stubState(isFavorite = false, isWatchlist = true)
      stubUpdateSuccess()
      advanceUntilIdle()

      viewModel.handleBtnFavorite(dataMediaItem)
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
