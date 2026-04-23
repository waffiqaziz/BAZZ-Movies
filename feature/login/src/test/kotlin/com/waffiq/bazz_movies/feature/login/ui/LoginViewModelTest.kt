package com.waffiq.bazz_movies.feature.login.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarItem
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarTMDb
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.model.account.Gravatar
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
    private const val TEST_TOKEN = "test_token"
    private const val TEST_SESSION = "test_session"
  }

  @Before
  fun setup() {
    viewModel = LoginViewModel(mockAuthTMDbAccountUseCase,mockUserPrefUseCase)

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

  // helper methods to reduce duplication
  private fun mockSuccessfulTokenCreation() {
    coEvery { mockAuthTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = TEST_TOKEN))
    )
  }

  private fun mockSuccessfulLogin() {
    coEvery {
      mockAuthTMDbAccountUseCase.login(TEST_USER, TEST_PASS, TEST_TOKEN)
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = TEST_TOKEN))
    )
  }

  private fun mockSuccessfulSessionCreation() {
    coEvery {
      mockAuthTMDbAccountUseCase.createSessionLogin(TEST_TOKEN)
    } returns flowOf(
      Outcome.Success(CreateSession(success = true, sessionId = TEST_SESSION))
    )
  }

  private fun mockSuccessfulUserDetail() {
    coEvery {
      mockAuthTMDbAccountUseCase.getAccountDetails(TEST_SESSION)
    } returns flowOf(
      Outcome.Success(AccountDetails(id = 1, username = TEST_USER))
    )
  }

  // verification helper methods
  private fun verifyTokenCreation() {
    coVerify { mockAuthTMDbAccountUseCase.createToken() }
  }

  private fun verifyFullLoginSequence() {
    coVerifySequence {
      mockAuthTMDbAccountUseCase.createToken()
      mockAuthTMDbAccountUseCase.login(TEST_USER, TEST_PASS, TEST_TOKEN)
      mockAuthTMDbAccountUseCase.createSessionLogin(TEST_TOKEN)
      mockAuthTMDbAccountUseCase.getAccountDetails(TEST_SESSION)
    }
  }

  private fun verifyUpToLoginSequence() {
    coVerifySequence {
      mockAuthTMDbAccountUseCase.createToken()
      mockAuthTMDbAccountUseCase.login(TEST_USER, TEST_PASS, TEST_TOKEN)
    }
  }

  private fun verifyUpToSessionCreationSequence() {
    coVerifySequence {
      mockAuthTMDbAccountUseCase.createToken()
      mockAuthTMDbAccountUseCase.login(TEST_USER, TEST_PASS, TEST_TOKEN)
      mockAuthTMDbAccountUseCase.createSessionLogin(TEST_TOKEN)
    }
  }

  @Test
  fun userLogin_whenUnsuccessful_emitsLoadingAndErrorStates() = runTest {
    // mock the UseCase to emit a failure
    coEvery { mockAuthTMDbAccountUseCase.createToken() } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("Token creation failed"))
    }

    // trigger the login function
    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    // Verify the states
    assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
    assertThat(errorStates).contains("Token creation failed") // error message captured

    // verify interactions with use case
    verifyTokenCreation()
  }

  @Test
  fun userLogin_whenSuccessful_triggersLoginAndSessionCreation() = runTest {
    mockSuccessfulTokenCreation()
    mockSuccessfulLogin()
    mockSuccessfulSessionCreation()
    mockSuccessfulUserDetail()

    // trigger
    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    // verify states
    assertThat(loginStates).containsExactly(false, true)

    // verify call sequence
    verifyFullLoginSequence()
  }

  @Test
  fun userLogin_whenResponseSuccessButAuthFails_setsLoginStateFalse() = runTest {
    coEvery { mockAuthTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = false, requestToken = null))
    )

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertEquals(listOf(false, false), loginStates) // initial value false + unchanged false
    verifyTokenCreation()
  }

  @Test
  fun userLogin_whenResponseSuccessButNullToken_setsLoginStateFalse() = runTest {
    coEvery { mockAuthTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = null))
    )

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertEquals(listOf(false, false), loginStates) // initial value false + unchanged false
    verifyTokenCreation()
  }

  @Test
  fun login_whenSuccessful_requestsTokenNull() = runTest {
    mockSuccessfulTokenCreation()

    coEvery {
      mockAuthTMDbAccountUseCase.login(TEST_USER, TEST_PASS, TEST_TOKEN)
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = null))
    )

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertThat(loginStates).containsExactly(false, false)

    // verify calls to the use case
    verifyUpToLoginSequence()
  }

  @Test
  fun login_whenUnsuccessful_emitsLoadingAndErrorStates() = runTest {
    mockSuccessfulTokenCreation()

    coEvery {
      mockAuthTMDbAccountUseCase.login(TEST_USER, TEST_PASS, TEST_TOKEN)
    } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("Token creation failed"))
    }

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
    assertThat(errorStates).contains("Token creation failed") // error message captured

    verifyUpToLoginSequence()
  }

  @Test
  fun createSession_whenResponseSuccessButAuthFails_setsLoginStateFalse() = runTest {
    mockSuccessfulTokenCreation()
    mockSuccessfulLogin()

    coEvery {
      mockAuthTMDbAccountUseCase.createSessionLogin(TEST_TOKEN)
    } returns flowOf(
      Outcome.Success(CreateSession(success = false, sessionId = TEST_SESSION)) // success false
    )

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertThat(loginStates).containsExactly(false, false)

    verifyUpToSessionCreationSequence()
  }

  @Test
  fun createSession_whenErrorOccurs_setsLoadingAndErrorStates() = runTest {
    mockSuccessfulTokenCreation()
    mockSuccessfulLogin()

    coEvery {
      mockAuthTMDbAccountUseCase.createSessionLogin(TEST_TOKEN)
    } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("Create login token failed"))
    }

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
    assertThat(errorStates).contains("Create login token failed") // error message captured

    verifyUpToSessionCreationSequence()
  }

  @Test
  fun getAccountDetails_whenSuccessful_setsLoginStateTrue() = runTest {
    mockSuccessfulTokenCreation()
    mockSuccessfulLogin()
    mockSuccessfulSessionCreation()

    coEvery {
      mockAuthTMDbAccountUseCase.getAccountDetails(TEST_SESSION)
    } returns flowOf(
      Outcome.Success(
        AccountDetails(
          id = null,
          username = TEST_USER,
          avatarItem = AvatarItem(
            avatarTMDb = AvatarTMDb(avatarPath = null),
            gravatar = Gravatar(hash = null)
          )
        )
      )
    )

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertThat(loginStates).containsExactly(false, true)
    assertThat(loadingStates).containsExactly(false)

    verifyFullLoginSequence()
  }

  @Test
  fun getAccountDetails_whenResponseSuccessButAvatarNull_returnsUserData() = runTest {
    mockSuccessfulTokenCreation()
    mockSuccessfulLogin()
    mockSuccessfulSessionCreation()

    coEvery {
      mockAuthTMDbAccountUseCase.getAccountDetails(TEST_SESSION)
    } returns flowOf(
      Outcome.Success(
        AccountDetails(
          id = null,
          username = TEST_USER,
          avatarItem = AvatarItem(
            avatarTMDb = null,
            gravatar = null
          )
        )
      )
    )

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertThat(loginStates).containsExactly(false, true)
    assertThat(loadingStates).containsExactly(false)

    verifyFullLoginSequence()
  }

  @Test
  fun getAccountDetails_whenUnsuccessful_emitsLoadingAndErrorStates() = runTest {
    mockSuccessfulTokenCreation()
    mockSuccessfulLogin()
    mockSuccessfulSessionCreation()

    coEvery {
      mockAuthTMDbAccountUseCase.getAccountDetails(TEST_SESSION)
    } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("Can't get user details"))
    }

    viewModel.userLogin(TEST_USER, TEST_PASS)
    advanceUntilIdle()

    assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
    assertThat(errorStates).contains("Can't get user details") // error message captured

    verifyFullLoginSequence()
  }

  @Test
  fun saveGuestUserPref_withCorrectValue_shouldSaveCorrectly() = runTest {
    viewModel.saveGuestUserPref("name", "username")
    advanceUntilIdle()
    coVerify {
      mockUserPrefUseCase.saveUserPref(
        match { userModel ->
          userModel.userId == 0 &&
            userModel.name == "name" &&
            userModel.username == "username" &&
            userModel.isLogin
        }
      )
    }
  }
}
