package com.waffiq.bazz_movies.feature.login.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarItem
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarTMDb
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.model.account.Gravatar
import com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount.AuthTMDbAccountUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
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

  @Test
  fun userLogin_outcomeSuccessDataFailed_setLoginStateFalse() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = false, requestToken = null))
    )

    val loginStates = mutableListOf<Boolean>()
    viewModel.loginState.observeForever { loginStates.add(it) }
    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle()

    assertEquals(
      listOf(false, false),
      loginStates
    ) // list of false false, has an initial value of false
    coVerifySequence {
      authTMDbAccountUseCase.createToken()
    }
  }

  @Test
  fun userLogin_outcomeSuccessTokenNull_setLoginStateFalse() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = null))
    )

    val loginStates = mutableListOf<Boolean>()
    viewModel.loginState.observeForever { loginStates.add(it) }
    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle()

    assertEquals(
      listOf(false, false),
      loginStates
    ) // list of false false, has an initial value of false
    coVerifySequence {
      authTMDbAccountUseCase.createToken()
    }
  }

  @Test
  fun login_success_requestTokenNull() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = null))
    )

    val loginStates = mutableListOf<Boolean>()
    viewModel.loginState.observeForever { loginStates.add(it) }

    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle()

    assertThat(loginStates).containsExactly(false, false)

    // Verify calls to the use case
    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    }
  }

  @Test
  fun login_emitsLoadingAndErrorStates_onFailure() = runTest {
    // Mock the UseCase to emit a failure
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flow {
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

    assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
    assertThat(errorStates).contains("Token creation failed") // error message captured

    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    }
  }

  @Test
  fun createSession_outcomeSuccessDataFailed_setLoginStateFalse() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.createSessionLogin("test_token")
    } returns flowOf(
      Outcome.Success(CreateSession(success = false, sessionId = "test_session")) // success false
    )

    val loginStates = mutableListOf<Boolean>()
    viewModel.loginState.observeForever { loginStates.add(it) }

    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle() // ensure all coroutines complete

    assertThat(loginStates).containsExactly(false, false)

    // Verify calls to the use case
    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
      authTMDbAccountUseCase.createSessionLogin("test_token")
    }
  }

  @Test
  fun createSession_outcomeSuccess_setUserModelAllPossibility() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.createSessionLogin("test_token")
    } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("Create login token failed"))
    }

    val loadingStates = mutableListOf<Boolean>()
    viewModel.loadingState.observeForever { loadingStates.add(it) }

    val errorStates = mutableListOf<String?>()
    viewModel.errorState.observeForever { errorStates.add(it) }

    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle()

    assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
    assertThat(errorStates).contains("Create login token failed") // error message captured

    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
      authTMDbAccountUseCase.createSessionLogin("test_token")
    }
  }

  @Test
  fun getUserDetail_outcomeSuccess_setLoginStateTrue() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.createSessionLogin("test_token")
    } returns flowOf(
      Outcome.Success(CreateSession(success = true, sessionId = "test_session")) // success false
    )

    coEvery {
      authTMDbAccountUseCase.getUserDetail("test_session")
    } returns flowOf(
      Outcome.Success(
        AccountDetails(
          id = null,
          username = "test_user",
          avatarItem = AvatarItem(
            avatarTMDb = AvatarTMDb(avatarPath = null),
            gravatar = Gravatar(hash = null)
          )
        )
      )
    )

    val loginStates = mutableListOf<Boolean>()
    viewModel.loginState.observeForever { loginStates.add(it) }

    val loadingStates = mutableListOf<Boolean>()
    viewModel.loadingState.observeForever { loadingStates.add(it) }

    val errorStates = mutableListOf<String?>()
    viewModel.errorState.observeForever { errorStates.add(it) }

    val userModel = mutableListOf<UserModel?>()
    viewModel.userModel.observeForever { userModel.add(it) }

    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle()

    assertThat(loginStates).containsExactly(false, true)
    assertThat(loadingStates).containsExactly(false)
    assertEquals(0, userModel[0]?.userId)

    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
      authTMDbAccountUseCase.createSessionLogin("test_token")
      authTMDbAccountUseCase.getUserDetail("test_session")
    }
  }

  @Test
  fun getUserDetail_outcomeSuccessAvatarNull() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.createSessionLogin("test_token")
    } returns flowOf(
      Outcome.Success(CreateSession(success = true, sessionId = "test_session")) // success false
    )

    coEvery {
      authTMDbAccountUseCase.getUserDetail("test_session")
    } returns flowOf(
      Outcome.Success(
        AccountDetails(
          id = null,
          username = "test_user",
          avatarItem = AvatarItem(
            avatarTMDb = null,
            gravatar = null
          )
        )
      )
    )

    val loginStates = mutableListOf<Boolean>()
    viewModel.loginState.observeForever { loginStates.add(it) }

    val loadingStates = mutableListOf<Boolean>()
    viewModel.loadingState.observeForever { loadingStates.add(it) }

    val errorStates = mutableListOf<String?>()
    viewModel.errorState.observeForever { errorStates.add(it) }

    val userModel = mutableListOf<UserModel?>()
    viewModel.userModel.observeForever { userModel.add(it) }

    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle()

    assertThat(loginStates).containsExactly(false, true)
    assertThat(loadingStates).containsExactly(false)
    assertEquals(0, userModel[0]?.userId)
    assertNull(userModel[0]?.tmdbAvatar)
    assertNull(userModel[0]?.gravatarHast)

    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
      authTMDbAccountUseCase.createSessionLogin("test_token")
      authTMDbAccountUseCase.getUserDetail("test_session")
    }
  }

  @Test
  fun getUserDetail_emitsLoadingAndErrorStates_onFailure() = runTest {
    coEvery { authTMDbAccountUseCase.createToken() } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
    } returns flowOf(
      Outcome.Success(Authentication(success = true, requestToken = "test_token"))
    )

    coEvery {
      authTMDbAccountUseCase.createSessionLogin("test_token")
    } returns flowOf(
      Outcome.Success(CreateSession(success = true, sessionId = "test_session"))
    )

    coEvery {
      authTMDbAccountUseCase.getUserDetail("test_session")
    } returns flow {
      emit(Outcome.Loading)
      emit(Outcome.Error("Can't get user details"))
    }

    val loadingStates = mutableListOf<Boolean>()
    viewModel.loadingState.observeForever { loadingStates.add(it) }

    val errorStates = mutableListOf<String?>()
    viewModel.errorState.observeForever { errorStates.add(it) }

    viewModel.userLogin("test_user", "test_pass")

    advanceUntilIdle()

    assertThat(loadingStates).containsExactly(true, false) // loading started, then stopped
    assertThat(errorStates).contains("Can't get user details") // error message captured

    coVerifySequence {
      authTMDbAccountUseCase.createToken()
      authTMDbAccountUseCase.login("test_user", "test_pass", "test_token")
      authTMDbAccountUseCase.createSessionLogin("test_token")
      authTMDbAccountUseCase.getUserDetail("test_session")
    }
  }
}
