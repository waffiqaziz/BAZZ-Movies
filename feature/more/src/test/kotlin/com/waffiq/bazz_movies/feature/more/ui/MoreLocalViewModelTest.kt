package com.waffiq.bazz_movies.feature.more.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MoreLocalViewModelTest {

  private val localDatabaseUseCase: LocalDatabaseUseCase = mockk()
  private lateinit var viewModel: MoreLocalViewModel
  private val testDispatcher = StandardTestDispatcher()

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setUp() {
    // Set the Main dispatcher to TestCoroutineDispatcher
    Dispatchers.setMain(testDispatcher)
    viewModel = MoreLocalViewModel(localDatabaseUseCase)
  }

  @After
  fun tearDown() {
    // Reset the Main dispatcher
    Dispatchers.resetMain()
  }

  @Test
  fun deleteAllPosts_whenSuccessful_emitsSuccessResult() = runTest {
    coEvery { localDatabaseUseCase.deleteAll() } returns DbResult.Success(1)

    viewModel.deleteAll()
    viewModel.state.test {
      assertEquals(UIState.Idle, awaitItem())
      assertEquals(UIState.Loading, awaitItem())
      assertEquals(UIState.Success(Unit), awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun deleteAllPosts_whenUnsuccessful_errorResult() = runTest {
    val errorMessage = "Delete failed"
    val expectedError = DbResult.Error(errorMessage)
    coEvery { localDatabaseUseCase.deleteAll() } returns expectedError

    viewModel.deleteAll()
    viewModel.state.test {
      assertEquals(UIState.Idle, awaitItem())
      assertEquals(UIState.Loading, awaitItem())
      assertEquals(UIState.Error(errorMessage), awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }
}
