package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import app.cash.turbine.test
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
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
        assertEquals(emptyList<PartsItem>(), awaitItem().parts)
        assertEquals(detailCollection.parts, awaitItem().parts)
        cancelAndIgnoreRemainingEvents()
      }
      coVerify { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) }
    }

  @Test
  fun loadMovieCollection_successFieldsNull_emitsSuccess() =
    runTest {
      coEvery { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) } returns
        successFlow(detailCollection.copy(parts = null, name = null, overview = null))

      collectionViewModel.loadMovieCollection(collectionId)

      collectionViewModel.uiState.test {
        assertEquals(emptyList<PartsItem>(), awaitItem().parts)

        val result = awaitItem()
        assertEquals(emptyList<PartsItem>(), result.parts)
        assertEquals("", result.name)
        assertEquals("", result.overview)
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
        assertEquals(emptyList<PartsItem>(), awaitItem().parts)
        assertEquals(true, awaitItem().isError)
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
        assertEquals(false, awaitItem().isLoading)
        assertEquals(true, awaitItem().isLoading)
        cancelAndIgnoreRemainingEvents()
      }
      coVerify { mockGetMovieCollectionUseCase.getMovieCollection(collectionId) }
    }
}
