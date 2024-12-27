package com.waffiq.bazz_movies.feature.more.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

  @Test
  fun `deleteSession emits NetworkResult success`() = testScope.runTest {
    val expectedResult = Outcome.Success(Post(success = true))
    coEvery { authTMDbAccountUseCase.deleteSession(sessionId) } returns flow {
      emit(expectedResult)
    }

    val results = mutableListOf<Outcome<Post>?>()
    val job = launch { viewModel.signOutState.toList(results) }
    viewModel.deleteSession(sessionId)
    advanceUntilIdle()

    assertEquals(listOf(null, expectedResult), results)
    job.cancel()
  }

  @Test
  fun `deleteSession emits NetworkResult error`() = testScope.runTest {
    val expectedError = Outcome.Error("Error deleting session")
    coEvery { authTMDbAccountUseCase.deleteSession(sessionId) } returns flow {
      emit(expectedError)
    }

    val results = mutableListOf<Outcome<Post>?>()
    val job = launch { viewModel.signOutState.toList(results) }
    viewModel.deleteSession(sessionId)
    advanceUntilIdle()

    assertEquals(listOf(null, expectedError), results)
    job.cancel()
  }

  @Test
  fun `removeState updates signOutState to loading`() = testScope.runTest {
    viewModel.removeState()
    advanceUntilIdle()

    val result = viewModel.signOutState.first() // `first()` collects the first emitted value
    assertEquals(Outcome.Loading, result)
  }
}
