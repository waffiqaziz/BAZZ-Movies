package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.detail.testutils.BaseMediaDetailViewModelTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailCollection
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionViewModelTest : BaseMediaDetailViewModelTest() {

  @Test
  fun loadMovieCollection_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) } returns
        successFlow(detailCollection)

      collectionViewModel.loadMovieCollection(collectionId)

      collectionViewModel.uiState.test {
        assertEquals(UIState.Idle, awaitItem()) // initiate state
        assertEquals(UIState.Success(detailCollection), awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
      coVerify { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) }
    }

  @Test
  fun loadMovieCollection_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) } returns errorFlow

      collectionViewModel.loadMovieCollection(collectionId)

      collectionViewModel.uiState.test {
        assertEquals(UIState.Idle, awaitItem())
        assertEquals(UIState.Error(errorMessage), awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
      coVerify { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) }
    }

  @Test
  fun loadMovieCollection_whenLoading_doesNothing() =
    runTest {
      coEvery { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) } returns loadingFlow

      collectionViewModel.loadMovieCollection(collectionId)

      collectionViewModel.uiState.test {
        assertEquals(UIState.Idle, awaitItem())
        assertEquals(UIState.Loading, awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
      coVerify { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) }
    }
}
