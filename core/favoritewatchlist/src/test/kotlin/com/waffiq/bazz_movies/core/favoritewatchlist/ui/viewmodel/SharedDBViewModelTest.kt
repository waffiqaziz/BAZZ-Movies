package com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.favoritewatchlist.LiveDataCollectors
import com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort.GuestFavoriteSortOption
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favoriteMoviesList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favoriteTvList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.watchlistMovieList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.watchlistTvList
import com.waffiq.bazz_movies.core.models.Favorite
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedDBViewModelTest {

  private lateinit var viewModel: SharedDBViewModel
  private val localDatabaseUseCase: FavoriteLocalDatabaseUseCase = mockk()

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
      assertNotNull(favoriteTvFromDB)
      assertNotNull(favoriteMoviesFromDB)
      assertNotNull(watchlistMoviesDB)
      assertNotNull(watchlistTvSeriesDB)
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
  fun insertToDB_whenSuccessful_emitsSuccess() =
    runTest {
      val dbResult = DbResult.Success(1)
      coEvery { localDatabaseUseCase.insertToDB(favorite) } returns dbResult

      val results = collectDbResults {
        viewModel.insertToDB(favorite)
      }

      assertEquals(1, results.size)
      assertEquals(dbResult, results[0].peekContent())
      coVerify { localDatabaseUseCase.insertToDB(favorite) }
    }

  @Test
  fun deleteFromDB_whenSuccessful_emitsSuccessAndSetsUndo() =
    runTest {
      testDatabaseOperationWithUndo(
        mockSetup = {
          coEvery { localDatabaseUseCase.deleteFromDB(any(), any()) } returns DbResult.Success(1)
        },
        operation = { viewModel.deleteFromDB(favorite) },
        verification = { coVerify { localDatabaseUseCase.deleteFromDB(any(), any()) } },
        dbResult = DbResult.Success(1),
      )
    }

  @Test
  fun update_whenSuccessful_emitsSuccessAndSetsUndo() =
    runTest {
      testDatabaseOperationWithUndo(
        mockSetup = {
          coEvery { localDatabaseUseCase.update(any()) } returns DbResult.Success(Unit)
        },
        operation = { viewModel.updateDB(favorite) },
        verification = {
          coVerify { localDatabaseUseCase.update(any()) }
        },
      )
    }

  @Test
  fun flowData_whenSuccessful_shouldTransformsToLiveData() =
    runTest {
      setupFlowsWithTestData()
      val viewModelWithData = SharedDBViewModel(localDatabaseUseCase)

      // collect all LiveData emissions
      val collectors = LiveDataCollectors(viewModelWithData)
      advanceUntilIdle()

      collectors.assertAllDataTransformed()
    }

  @Test
  fun updateSort_withSameOption_doNotChangeCurrentSort() {
    viewModel.updateSort(GuestFavoriteSortOption.RECENTLY_ADDED)
    assertEquals(GuestFavoriteSortOption.RECENTLY_ADDED, viewModel.currentSort.value)
  }

  @Test
  fun updateSort_byOldestFirst_changesCurrentSort() {
    viewModel.updateSort(GuestFavoriteSortOption.OLDEST_ADDED)
    assertEquals(GuestFavoriteSortOption.OLDEST_ADDED, viewModel.currentSort.value)
  }

  private fun TestScope.collectDbResults(operation: suspend () -> Unit): List<Event<DbResult<*>>> {
    val results = mutableListOf<Event<DbResult<*>>>()
    viewModel.dbResult.observeForever { results.add(it) }
    runBlocking { operation() }
    advanceUntilIdle()
    return results
  }

  private fun testDatabaseOperationWithUndo(
    mockSetup: () -> Unit,
    operation: suspend () -> Unit,
    verification: () -> Unit,
    dbResult: DbResult.Success<*> = DbResult.Success(Unit),
  ) = runTest {
    mockSetup()

    val dbResults = mutableListOf<Event<DbResult<*>>>()
    val undoResults = mutableListOf<Event<Favorite>>()

    viewModel.dbResult.observeForever { dbResults.add(it) }
    viewModel.undoDB.observeForever { undoResults.add(it) }

    operation()
    advanceUntilIdle()

    assertEquals(1, dbResults.size)
    assertEquals(dbResult, dbResults[0].peekContent())
    assertEquals(1, undoResults.size)
    assertEquals(favorite, undoResults[0].peekContent())

    verification()
  }

  private fun setupFlowsWithTestData() {
    every {
      localDatabaseUseCase.favoriteTvFromDB
    } returns MutableStateFlow(favoriteTvList)
    every {
      localDatabaseUseCase.favoriteMoviesFromDB
    } returns MutableStateFlow(favoriteMoviesList)
    every {
      localDatabaseUseCase.watchlistTvFromDB
    } returns MutableStateFlow(watchlistTvList)
    every {
      localDatabaseUseCase.watchlistMovieFromDB
    } returns MutableStateFlow(watchlistMovieList)
  }
}
