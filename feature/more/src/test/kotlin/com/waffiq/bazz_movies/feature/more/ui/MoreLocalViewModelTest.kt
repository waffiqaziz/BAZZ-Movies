package com.waffiq.bazz_movies.feature.more.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
  fun `deleteAll posts success result`() = runTest {
    // Arrange
    val expectedResult = DbResult.Success(1)
    coEvery { localDatabaseUseCase.deleteAll() } returns expectedResult

    val observer = mockk<Observer<Event<DbResult<Int>>>>(relaxed = true)
    viewModel.dbResult.observeForever(observer)

    // Act
    viewModel.deleteAll()
    testDispatcher.scheduler.advanceUntilIdle() // Ensures coroutines complete

    // Assert
    verify {
      observer.onChanged(
        match {
          it.getContentIfNotHandled() == expectedResult
        }
      )
    }

    // Cleanup
    viewModel.dbResult.removeObserver(observer)
  }

  @Test
  fun `deleteAll posts error result`() = runTest {
    val expectedError = DbResult.Error("Delete failed")
    coEvery { localDatabaseUseCase.deleteAll() } returns expectedError

    val observer = mockk<Observer<Event<DbResult<Int>>>>(relaxed = true)
    viewModel.dbResult.observeForever(observer)
    viewModel.deleteAll()
    testDispatcher.scheduler.advanceUntilIdle()

    verify {
      observer.onChanged(
        match {
          it.getContentIfNotHandled() == expectedError
        }
      )
    }

    viewModel.dbResult.removeObserver(observer)
  }
}
