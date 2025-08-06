package com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.LiveDataCollectors
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.TestData
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedDBViewModelTest {

  private lateinit var viewModel: SharedDBViewModel
  private val localDatabaseUseCase: LocalDatabaseUseCase = mockk()

  // test data collections
  private val testData = TestData()
  private val baseFavorite = testData.baseFavorite

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    setupMockDefaults()
    viewModel = SharedDBViewModel(localDatabaseUseCase)
  }

  private fun setupMockDefaults() {
    every { localDatabaseUseCase.favoriteTvFromDB } returns flowOf(emptyList())
    every { localDatabaseUseCase.favoriteMoviesFromDB } returns flowOf(emptyList())
    every { localDatabaseUseCase.watchlistMovieFromDB } returns flowOf(emptyList())
    every { localDatabaseUseCase.watchlistTvFromDB } returns flowOf(emptyList())
  }

  @Test
  fun localDatabaseUseCase_whenCalled_returnsFlowCorrectly() {
    // Verify LiveData properties are initialized
    with(viewModel) {
      assertThat(favoriteTvFromDB).isNotNull()
      assertThat(favoriteMoviesFromDB).isNotNull()
      assertThat(watchlistMoviesDB).isNotNull()
      assertThat(watchlistTvSeriesDB).isNotNull()
    }

    // Verify correct flows are accessed
    verifyAll {
      localDatabaseUseCase.favoriteTvFromDB
      localDatabaseUseCase.favoriteMoviesFromDB
      localDatabaseUseCase.watchlistMovieFromDB
      localDatabaseUseCase.watchlistTvFromDB
    }
  }

  @Test
  fun insertToDB_whenSuccessful_emitsSuccess() = runTest {
    val dbResult = DbResult.Success(1)
    coEvery { localDatabaseUseCase.insertToDB(baseFavorite) } returns dbResult

    val results = collectDbResults {
      viewModel.insertToDB(baseFavorite)
    }

    assertThat(results).hasSize(1)
    assertThat(results[0].peekContent()).isEqualTo(dbResult)
    coVerify { localDatabaseUseCase.insertToDB(baseFavorite) }
  }

  @Test
  fun delFromFavoriteDB_whenSuccessful_emitsSuccessAndSetsUndo() = runTest {
    testDatabaseOperationWithUndo(
      mockSetup = {
        coEvery {
          localDatabaseUseCase.deleteFromDB(baseFavorite)
        } returns DbResult.Success(1)
      },
      operation = { viewModel.delFromFavoriteDB(baseFavorite) },
      verification = {
        coVerify { localDatabaseUseCase.deleteFromDB(baseFavorite) }
      }
    )
  }

  @Test
  fun updateToFavoriteDB_whenSuccessful_emitsSuccessAndSetsUndo() = runTest {
    testDatabaseOperationWithUndo(
      mockSetup = {
        coEvery {
          localDatabaseUseCase.updateFavoriteItemDB(false, baseFavorite)
        } returns DbResult.Success(1)
      },
      operation = { viewModel.updateToFavoriteDB(baseFavorite) },
      verification = {
        coVerify { localDatabaseUseCase.updateFavoriteItemDB(false, baseFavorite) }
      }
    )
  }

  @Test
  fun updateToWatchlistDB_whenSuccessful_emitsSuccessAndSetsUndo() = runTest {
    testDatabaseOperationWithUndo(
      mockSetup = {
        coEvery {
          localDatabaseUseCase.updateWatchlistItemDB(false, baseFavorite)
        } returns DbResult.Success(1)
      },
      operation = { viewModel.updateToWatchlistDB(baseFavorite) },
      verification = {
        coVerify { localDatabaseUseCase.updateWatchlistItemDB(false, baseFavorite) }
      }
    )
  }

  @Test
  fun updateToRemoveFromWatchlistDB_whenSuccessful_emitsSuccessAndSetsUndo() = runTest {
    testDatabaseOperationWithUndo(
      mockSetup = {
        coEvery {
          localDatabaseUseCase.updateWatchlistItemDB(true, baseFavorite)
        } returns DbResult.Success(1)
      },
      operation = { viewModel.updateToRemoveFromWatchlistDB(baseFavorite) },
      verification = {
        coVerify { localDatabaseUseCase.updateWatchlistItemDB(true, baseFavorite) }
      }
    )
  }

  @Test
  fun updateToRemoveFromFavoriteDB_whenSuccessful_emitsSuccessAndSetsUndo() = runTest {
    testDatabaseOperationWithUndo(
      mockSetup = {
        coEvery {
          localDatabaseUseCase.updateFavoriteItemDB(true, baseFavorite)
        } returns DbResult.Success(1)
      },
      operation = { viewModel.updateToRemoveFromFavoriteDB(baseFavorite) },
      verification = {
        coVerify { localDatabaseUseCase.updateFavoriteItemDB(true, baseFavorite) }
      }
    )
  }

  @Test
  fun flowData_whenSuccessful_shouldTransformsToLiveData() = runTest {
    setupFlowsWithTestData()
    val viewModelWithData = SharedDBViewModel(localDatabaseUseCase)

    // collect all LiveData emissions
    val collectors = LiveDataCollectors(viewModelWithData)
    advanceUntilIdle()

    collectors.assertAllDataTransformed(testData)
  }

  private fun TestScope.collectDbResults(operation: suspend () -> Unit): List<Event<DbResult<Int>>> {
    val results = mutableListOf<Event<DbResult<Int>>>()
    viewModel.dbResult.observeForever { results.add(it) }
    runBlocking { operation() }
    advanceUntilIdle()
    return results
  }

  private fun testDatabaseOperationWithUndo(
    mockSetup: () -> Unit,
    operation: suspend () -> Unit,
    verification: () -> Unit,
  ) = runTest {
    val dbResult = DbResult.Success(1)
    mockSetup()

    val dbResults = mutableListOf<Event<DbResult<Int>>>()
    val undoResults = mutableListOf<Event<Favorite>>()

    viewModel.dbResult.observeForever { dbResults.add(it) }
    viewModel.undoDB.observeForever { undoResults.add(it) }

    operation()
    advanceUntilIdle()

    assertThat(dbResults).hasSize(1)
    assertThat(dbResults[0].peekContent()).isEqualTo(dbResult)
    assertThat(undoResults).hasSize(1)
    assertThat(undoResults[0].peekContent()).isEqualTo(baseFavorite)

    verification()
  }

  private fun setupFlowsWithTestData() {
    every { localDatabaseUseCase.favoriteTvFromDB } returns MutableStateFlow(testData.favoriteTvList)
    every { localDatabaseUseCase.favoriteMoviesFromDB } returns MutableStateFlow(testData.favoriteMoviesList)
    every { localDatabaseUseCase.watchlistTvFromDB } returns MutableStateFlow(testData.watchlistTvList)
    every { localDatabaseUseCase.watchlistMovieFromDB } returns MutableStateFlow(testData.watchlistMovieList)
  }
}
