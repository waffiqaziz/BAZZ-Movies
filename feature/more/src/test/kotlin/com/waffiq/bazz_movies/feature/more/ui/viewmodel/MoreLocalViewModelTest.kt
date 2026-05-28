package com.waffiq.bazz_movies.feature.more.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.net.toUri
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.domain.usecase.DatabaseBackupUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class MoreLocalViewModelTest {

  private val mockLocalDatabaseUseCase: FavoriteLocalDatabaseUseCase = mockk()
  private val mockSearchHistoryLocalDatabaseUseCase: SearchHistoryLocalDatabaseUseCase = mockk()
  private val mockDatabaseBackupUseCase: DatabaseBackupUseCase = mockk()
  private val mockSuccess = DbResult.Success(Unit)

  private lateinit var viewModel: MoreLocalViewModel
  private val testDispatcher = StandardTestDispatcher()

  private val errorMessage = "failed"
  private val expectedError = DbResult.Error(errorMessage)

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    viewModel = MoreLocalViewModel(
      mockLocalDatabaseUseCase,
      mockSearchHistoryLocalDatabaseUseCase,
      mockDatabaseBackupUseCase,
    )
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun deleteAllPosts_whenSuccessful_emitsSuccessResult() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteAll() } returns DbResult.Success(1)

      viewModel.deleteAll()
      viewModel.state.test {
        assertEquals(UIState.Idle, awaitItem())
        assertEquals(UIState.Loading, awaitItem())
        assertEquals(UIState.Success(Unit), awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun deleteAllPosts_whenUnsuccessful_errorResult() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteAll() } returns expectedError

      viewModel.deleteAll()
      viewModel.state.test {
        assertEquals(UIState.Idle, awaitItem())
        assertEquals(UIState.Loading, awaitItem())
        assertEquals(UIState.Error(errorMessage), awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun deleteAllSearchHistory_whenCalled_callsCorrectFunction() =
    runTest {
      coEvery { mockSearchHistoryLocalDatabaseUseCase.deleteAll() } returns 1

      viewModel.deleteAllSearchHistory()
      advanceUntilIdle()

      coVerify { mockSearchHistoryLocalDatabaseUseCase.deleteAll() }
    }

  @Test
  fun backupDatabase_whenCalled_callsCorrectFunction() =
    runTest {
      coEvery { mockDatabaseBackupUseCase.backupDatabase(any()) } returns mockSuccess

      viewModel.backupState.test {
        assertEquals(UIState.Idle, awaitItem())
        viewModel.backupDatabase("".toUri())
        assertEquals(UIState.Loading, awaitItem())
        assertEquals(UIState.Success(Unit), awaitItem())
        viewModel.resetBackupState()
        assertEquals(UIState.Idle, awaitItem())
        cancelAndIgnoreRemainingEvents()
      }

      coVerify { mockDatabaseBackupUseCase.backupDatabase(any()) }
    }

  @Test
  fun backupDatabase_whenUnsuccessful_errorResult() =
    runTest {
      coEvery { mockDatabaseBackupUseCase.backupDatabase(any()) } returns expectedError

      viewModel.backupDatabase("".toUri())
      viewModel.backupState.test {
        assertEquals(UIState.Idle, awaitItem())
        assertEquals(UIState.Loading, awaitItem())
        assertEquals(UIState.Error(errorMessage), awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun restoreDatabase_whenCalled_emitsLoadingAndSuccess() =
    runTest {
      coEvery { mockDatabaseBackupUseCase.restoreDatabase(any()) } returns mockSuccess

      viewModel.restoreState.test {
        assertEquals(UIState.Idle, awaitItem())
        viewModel.restoreDatabase("".toUri())
        assertEquals(UIState.Loading, awaitItem())
        assertEquals(UIState.Success(Unit), awaitItem())
        viewModel.resetRestoreState()
        assertEquals(UIState.Idle, awaitItem())
        cancelAndIgnoreRemainingEvents()
      }

      coVerify { mockDatabaseBackupUseCase.restoreDatabase(any()) }
    }

  @Test
  fun restoreDatabase_whenUnsuccessful_errorResult() =
    runTest {
      coEvery { mockDatabaseBackupUseCase.restoreDatabase(any()) } returns expectedError

      viewModel.restoreDatabase("".toUri())
      viewModel.restoreState.test {
        assertEquals(UIState.Idle, awaitItem())
        assertEquals(UIState.Loading, awaitItem())
        assertEquals(UIState.Error(errorMessage), awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
    }
}
