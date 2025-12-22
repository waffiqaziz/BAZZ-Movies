package com.waffiq.bazz_movies.feature.more.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoreUserViewModelTest {

  private val sessionId = "session_id"

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase = mockk()
  private lateinit var viewModel: MoreUserViewModel

  private val testDispatcher = StandardTestDispatcher()
  private val testScope = TestScope(testDispatcher)

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    viewModel = MoreUserViewModel(authTMDbAccountUseCase)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun deleteSession_whenSuccessful_returnsSuccess() = testScope.runTest {
    val expectedResult = Outcome.Success(Post(success = true))
    coEvery { authTMDbAccountUseCase.deleteSession(sessionId) } returns flowOf(expectedResult)

    viewModel.deleteSession(sessionId)
    viewModel.state.test {
      assertEquals(UIState.Idle, awaitItem())
      assertEquals(UIState.Success(Unit), awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun deleteSession_whenUnsuccessful_returnsError() = testScope.runTest {
    val errorMessage = "Error deleting session"
    val expectedError = Outcome.Error(errorMessage)
    coEvery { authTMDbAccountUseCase.deleteSession(sessionId) } returns flowOf(expectedError)

    viewModel.deleteSession(sessionId)
    viewModel.state.test {
      assertEquals(UIState.Idle, awaitItem())
      assertEquals(UIState.Error(errorMessage), awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun deleteSession_whenLoading_returnsLoading() = testScope.runTest {
    val expectedLoading = Outcome.Loading
    coEvery { authTMDbAccountUseCase.deleteSession(sessionId) } returns flowOf(expectedLoading)

    viewModel.deleteSession(sessionId)
    viewModel.state.test {
      assertEquals(UIState.Idle, awaitItem())
      assertEquals(UIState.Loading, awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun removeState_whenCalled_updatesSignOutStateToLoading() = testScope.runTest {
    viewModel.removeState()
    viewModel.state.test {
      assertEquals(UIState.Idle, awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }
}
