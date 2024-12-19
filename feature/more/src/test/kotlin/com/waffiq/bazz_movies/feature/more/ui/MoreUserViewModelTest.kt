package com.waffiq.bazz_movies.feature.more.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.waffiq.bazz_movies.core.data.Post
import com.waffiq.bazz_movies.core.network.data.remote.post_body.SessionIDPostModel
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
class MoreUserViewModelTest {

  // Rule to allow LiveData to work properly in tests
  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  // Mock dependencies
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase = mockk()
  private lateinit var viewModel: MoreUserViewModel

  // Test dispatcher and scope
  private val testDispatcher = StandardTestDispatcher()
  private val testScope = TestScope(testDispatcher)

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    viewModel = MoreUserViewModel(authTMDbAccountUseCase)
  }

  @Test
  fun `deleteSession emits NetworkResult success`() = testScope.runTest {
    // Arrange
    val sessionIDPostModel = SessionIDPostModel("test_session_id")
    val expectedResult = NetworkResult.Success(Post(success = true))
    coEvery { authTMDbAccountUseCase.deleteSession(sessionIDPostModel) } returns flow {
      emit(expectedResult)
    }

    // Act
    val results = mutableListOf<NetworkResult<Post>?>()
    val job = launch { viewModel.signOutState.toList(results) }
    viewModel.deleteSession(sessionIDPostModel)
    advanceUntilIdle()

    // Assert
    assertEquals(listOf(null, expectedResult), results)
    job.cancel()
  }

  @Test
  fun `deleteSession emits NetworkResult error`() = testScope.runTest {
    val sessionIDPostModel = SessionIDPostModel("test_session_id")
    val expectedError = NetworkResult.Error("Error deleting session")
    coEvery { authTMDbAccountUseCase.deleteSession(sessionIDPostModel) } returns flow {
      emit(expectedError)
    }

    val results = mutableListOf<NetworkResult<Post>?>()
    val job = launch { viewModel.signOutState.toList(results) }
    viewModel.deleteSession(sessionIDPostModel)
    advanceUntilIdle()

    assertEquals(listOf(null, expectedError), results)
    job.cancel()
  }

  @Test
  fun `removeState updates signOutState to loading`() = testScope.runTest {
    viewModel.removeState()

    // Advance the dispatcher to allow all coroutines to complete
    advanceUntilIdle()

    val result = viewModel.signOutState.first() // `first()` collects the first emitted value
    assertEquals(NetworkResult.Loading, result)
  }
}
