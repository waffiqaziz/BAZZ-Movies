package com.waffiq.bazz_movies.feature.login.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthenticationViewModelTest {

  private lateinit var viewModel: AuthenticationViewModel
  private val authTMDbAccountUseCase: AuthTMDbAccountUseCase = mockk()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule() // For LiveData testing

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    viewModel = AuthenticationViewModel(authTMDbAccountUseCase)
  }

  @Test
  fun userLogin_emitsLoadingAndErrorStates_onFailure() = runTest {
    // Mock the UseCase to emit a failure
    coEvery { authTMDbAccountUseCase.createToken() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("Token creation failed"))
    }

    // Observe LiveData
    val loadingStates = mutableListOf<Boolean>()
    viewModel.loadingState.observeForever { loadingStates.add(it) }

    val errorStates = mutableListOf<String?>()
    viewModel.errorState.observeForever { errorStates.add(it) }

    // Trigger the login function
    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle() // Advance coroutine execution

    // Verify the states
    assertThat(loadingStates).containsExactly(true, false) // Loading started, then stopped
    assertThat(errorStates).contains("Token creation failed") // Error message captured

    // Verify interactions with use case
    coVerify { authTMDbAccountUseCase.createToken() }
  }

  @Test
  fun userLogin_success_triggersLoginAndSessionCreation() = runTest {
    // Mock token creation
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    // Mock login
    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    // Mock session creation
    coEvery {
      authTMDbAccountUseCase.createSessionLogin("test_token")
    } returns flowOf(
      Outcome.Success(CreateSession(success = true, sessionId = "test_session"))
    )

    // Mock user detail fetching
    coEvery {
      authTMDbAccountUseCase.getUserDetail("test_session")
    } returns flowOf(Outcome.Success(AccountDetails(id = 1, username = "test_user")))

    // Observe LiveData
    val loginStates = mutableListOf<Boolean>()
    viewModel.loginState.observeForever { loginStates.add(it) }

    // Trigger login
    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle() // Ensure all coroutines complete

    // Verify states
    assertThat(loginStates).containsExactly(false, true)

    // Verify calls to the use case
    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
      authTMDbAccountUseCase.createSessionLogin("test_token")
      authTMDbAccountUseCase.getUserDetail("test_session")
    }
  }
}
