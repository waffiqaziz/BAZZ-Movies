package com.waffiq.bazz_movies.feature.login.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

  private lateinit var viewModel: LoginViewModel
  private val mockAuthTMDbAccountUseCase: AuthTMDbAccountUseCase = mockk()
  private val mockUserPrefUseCase: UserPrefUseCase = mockk()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule() // for LiveData testing

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  // observer lists for LiveData
  private val loadingStates = mutableListOf<Boolean>()
  private val errorStates = mutableListOf<String?>()
  private val loginStates = mutableListOf<Boolean>()

  // test constants
  companion object {
    private const val TEST_USER = "test_user"
    private const val TEST_PASS = "test_pass"
  }

  @Before
  fun setup() {
    viewModel = LoginViewModel(mockAuthTMDbAccountUseCase, mockUserPrefUseCase)

    // reset observer lists
    loadingStates.clear()
    errorStates.clear()
    loginStates.clear()

    // set up observers
    viewModel.loadingState.observeForever { loadingStates.add(it) }
    viewModel.errorState.observeForever { errorStates.add(it) }
    viewModel.loginState.observeForever { loginStates.add(it) }
    coEvery { mockUserPrefUseCase.saveUserPref(any()) } just Runs
  }

  @Test
  fun userLogin_whenUnsuccessful_emitsLoadingAndErrorStates() =
    runTest {
      coEvery { mockAuthTMDbAccountUseCase.login(any(), any()) } returns flow {
        emit(Outcome.Loading)
        emit(Outcome.Error("Token creation failed"))
      }

      viewModel.userLogin(TEST_USER, TEST_PASS)
      advanceUntilIdle()

      assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
      assertThat(errorStates).contains("Token creation failed") // error message captured

      coVerify { mockAuthTMDbAccountUseCase.login(any(), any()) }
    }

  @Test
  fun userLogin_whenSuccessful_triggersLoginAndSessionCreation() =
    runTest {
      coEvery { mockAuthTMDbAccountUseCase.login(any(), any()) } returns flow {
        emit(Outcome.Loading)
        emit(Outcome.Success(Unit))
      }

      viewModel.userLogin(TEST_USER, TEST_PASS)
      advanceUntilIdle()

      assertEquals(true, loginStates[0])

      coVerify { mockAuthTMDbAccountUseCase.login(any(), any()) }
    }

  @Test
  fun saveGuestUserPref_withCorrectValue_shouldSaveCorrectly() =
    runTest {
      viewModel.saveGuestUserPref("name", "username")
      advanceUntilIdle()
      coVerify {
        mockUserPrefUseCase.saveUserPref(
          match { userModel ->
            userModel.userId == 0 &&
              userModel.name == "name" &&
              userModel.username == "username" &&
              userModel.isLogin
          },
        )
      }
    }
}
