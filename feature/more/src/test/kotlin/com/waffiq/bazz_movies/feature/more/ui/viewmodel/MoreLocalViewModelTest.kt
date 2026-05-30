package com.waffiq.bazz_movies.feature.more.ui.viewmodel

import androidx.core.net.toUri
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.domain.usecase.DatabaseBackupUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.more.testutils.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class MoreLocalViewModelTest : BaseViewModelTest() {

  private val dbResulError = DbResult.Error(errorMessage)
  private val dbResultSuccess = DbResult.Success(Unit)

  private val mockLocalDatabaseUseCase: FavoriteLocalDatabaseUseCase = mockk()
  private val mockSearchHistoryLocalDatabaseUseCase: SearchHistoryLocalDatabaseUseCase = mockk()
  private val mockDatabaseBackupUseCase: DatabaseBackupUseCase = mockk()

  private lateinit var viewModel: MoreLocalViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setUp() {
    viewModel = MoreLocalViewModel(
      mockLocalDatabaseUseCase,
      mockSearchHistoryLocalDatabaseUseCase,
      mockDatabaseBackupUseCase,
    )
  }

  @Test
  fun deleteAllPosts_whenSuccessful_emitsSuccessResult() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteAll() } coAnswers {
        yield()
        DbResult.Success(1)
      }

      viewModel.state.test {
        assertStateFlow(
          action = { viewModel.deleteAll() },
          expectedState = UIState.Success(Unit),
        )
      }
    }

  @Test
  fun deleteAllPosts_whenUnsuccessful_errorResult() =
    runTest {
      coEvery { mockLocalDatabaseUseCase.deleteAll() } coAnswers {
        yield()
        dbResulError
      }

      viewModel.state.test {
        assertStateFlow(
          action = { viewModel.deleteAll() },
          expectedState = UIState.Error(errorMessage),
        )
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
      coEvery { mockDatabaseBackupUseCase.backupDatabase(any()) } coAnswers {
        yield()
        dbResultSuccess
      }

      viewModel.backupState.test {
        assertStateFlowWithReset(
          action = { viewModel.backupDatabase("".toUri()) },
          expectedState = UIState.Success(Unit),
          resetAction = { viewModel.resetBackupState() },
        )
      }

      coVerify { mockDatabaseBackupUseCase.backupDatabase(any()) }
    }

  @Test
  fun backupDatabase_whenUnsuccessful_errorResult() =
    runTest {
      coEvery { mockDatabaseBackupUseCase.backupDatabase(any()) } coAnswers {
        yield()
        dbResulError
      }

      viewModel.backupState.test {
        assertStateFlow(
          action = { viewModel.backupDatabase("".toUri()) },
          expectedState = UIState.Error(errorMessage),
        )
      }
    }

  @Test
  fun restoreDatabase_whenCalled_emitsLoadingAndSuccess() =
    runTest {
      coEvery { mockDatabaseBackupUseCase.restoreDatabase(any()) } coAnswers {
        yield()
        dbResultSuccess
      }

      viewModel.restoreState.test {
        assertStateFlowWithReset(
          action = { viewModel.restoreDatabase("".toUri()) },
          expectedState = UIState.Success(Unit),
          resetAction = { viewModel.resetRestoreState() },
        )
      }

      coVerify { mockDatabaseBackupUseCase.restoreDatabase(any()) }
    }

  @Test
  fun restoreDatabase_whenUnsuccessful_errorResult() =
    runTest {
      coEvery { mockDatabaseBackupUseCase.restoreDatabase(any()) } coAnswers {
        yield()
        dbResulError
      }

      viewModel.restoreState.test {
        assertStateFlow(
          action = { viewModel.restoreDatabase("".toUri()) },
          expectedState = UIState.Error(errorMessage),
        )
      }
    }

  private suspend fun ReceiveTurbine<UIState<Unit>>.assertStateFlow(
    action: () -> Unit,
    expectedState: UIState<Unit>,
  ) {
    assertEquals(UIState.Idle, awaitItem())
    action()
    assertEquals(UIState.Loading, awaitItem())
    assertEquals(expectedState, awaitItem())
    cancelAndIgnoreRemainingEvents()
  }

  private suspend fun ReceiveTurbine<UIState<Unit>>.assertStateFlowWithReset(
    action: () -> Unit,
    expectedState: UIState<Unit>,
    resetAction: () -> Unit,
  ) {
    assertEquals(UIState.Idle, awaitItem())
    action()
    assertEquals(UIState.Loading, awaitItem())
    assertEquals(expectedState, awaitItem())
    resetAction()
    assertEquals(UIState.Idle, awaitItem())
    cancelAndIgnoreRemainingEvents()
  }
}
