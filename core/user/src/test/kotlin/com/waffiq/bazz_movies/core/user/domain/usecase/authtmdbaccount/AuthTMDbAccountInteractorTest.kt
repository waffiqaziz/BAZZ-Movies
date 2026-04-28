package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarItem
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarTMDb
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.model.account.Gravatar
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Test AuthTMDbAccountInteractor using mockito
 */
class AuthTMDbAccountInteractorTest {

  private val mockRepository: IUserRepository = mock()
  private lateinit var interactor: AuthTMDbAccountInteractor

  @Before
  fun setup() {
    interactor = AuthTMDbAccountInteractor(mockRepository)
  }

  companion object {
    private const val TEST_USER = "username"
    private const val TEST_PASS = "password"
    private const val TEST_TOKEN = "request_token"
    private const val TEST_SESSION = "session_id"
    private const val ERROR_MESSAGE = "Network error"
  }

  // region Helper
  private fun stubTokenSuccess() {
    whenever(mockRepository.createToken()).thenReturn(
      flowOf(Outcome.Success(Authentication(success = true, requestToken = TEST_TOKEN)))
    )
  }

  private fun stubLoginSuccess() {
    whenever(mockRepository.login(TEST_USER, TEST_PASS, TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Success(Authentication(success = true, requestToken = TEST_TOKEN)))
    )
  }

  private fun stubSessionSuccess() {
    whenever(mockRepository.createSessionLogin(TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Success(CreateSession(success = true, sessionId = TEST_SESSION)))
    )
  }

  private fun stubAccountDetailsSuccess() {
    whenever(mockRepository.getAccountDetails(TEST_SESSION)).thenReturn(
      flowOf(
        Outcome.Success(
          AccountDetails(
            id = 1,
            name = "Waffiq",
            username = TEST_USER,
            avatarItem = AvatarItem(
              gravatar = Gravatar(hash = "hash123"),
              avatarTMDb = AvatarTMDb(avatarPath = "/path.jpg")
            )
          )
        )
      )
    )
  }
  // endregion Helper

  private fun stubFullSuccess() {
    stubTokenSuccess()
    stubLoginSuccess()
    stubSessionSuccess()
    stubAccountDetailsSuccess()
  }

  // Success path

  @Test
  fun login_whenAllStepsSucceed_emitsLoadingThenSuccess() = runTest {
    stubFullSuccess()

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertTrue(awaitItem() is Outcome.Success)
      awaitComplete()
    }
  }

  @Test
  fun login_whenAllStepsSucceed_callsRepositoryInOrder() = runTest {
    stubFullSuccess()

    interactor.login(TEST_USER, TEST_PASS).collect()

    inOrder(mockRepository) {
      verify(mockRepository).createToken()
      verify(mockRepository).login(TEST_USER, TEST_PASS, TEST_TOKEN)
      verify(mockRepository).createSessionLogin(TEST_TOKEN)
      verify(mockRepository).getAccountDetails(TEST_SESSION)
      verify(mockRepository).saveUserPref(any())
    }
  }

  @Test
  fun login_whenAllStepsSucceed_savesCorrectUserModel() = runTest {
    stubFullSuccess()

    interactor.login(TEST_USER, TEST_PASS).collect()

    val captor = argumentCaptor<UserModel>()
    verify(mockRepository).saveUserPref(captor.capture())
    captor.firstValue.also { user ->
      assertEquals(1, user.userId)
      assertEquals("Waffiq", user.name)
      assertEquals(TEST_USER, user.username)
      assertEquals(TEST_SESSION, user.token)
      assertTrue(user.isLogin)
      assertEquals("hash123", user.gravatarHash)
      assertEquals("/path.jpg", user.tmdbAvatar)
    }
  }

  // Step 1: createToken

  @Test
  fun createToken_errorOutcome_emitErrorAndStopsChain() = runTest {
    whenever(mockRepository.createToken()).thenReturn(
      flowOf(Outcome.Error("Token creation failed"))
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Token creation failed", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository).createToken()
    verify(mockRepository, never()).login(any(), any(), any())
    verify(mockRepository, never()).createSessionLogin(any())
    verify(mockRepository, never()).getAccountDetails(any())
    verify(mockRepository, never()).saveUserPref(any())
  }

  @Test
  fun createToken_nullRequestTokenInSuccess_emitsErrorAndStopsChain() = runTest {
    whenever(mockRepository.createToken()).thenReturn(
      flowOf(Outcome.Success(Authentication(success = true, requestToken = null)))
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Request token was null", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository).createToken()
    verify(mockRepository, never()).login(any(), any(), any())
  }

  @Test
  fun createToken_unexpectedState_emitsUnexpectedStateError() = runTest {
    whenever(mockRepository.createToken()).thenReturn(flowOf(Outcome.Loading))

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Unexpected state", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository, never()).login(any(), any(), any())
  }

  // Step 2: login (authorize token)

  @Test
  fun authorize_errorOutcome_emitsErrorAndStopsChain() = runTest {
    stubTokenSuccess()
    whenever(mockRepository.login(TEST_USER, TEST_PASS, TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Error("Authorize token failed"))
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Authorize token failed", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository).login(TEST_USER, TEST_PASS, TEST_TOKEN)
    verify(mockRepository, never()).createSessionLogin(any())
    verify(mockRepository, never()).saveUserPref(any())
  }

  @Test
  fun authorize_nullRequestTokenInSuccess_emitsErrorAndStopsChain() = runTest {
    stubTokenSuccess()
    whenever(mockRepository.login(TEST_USER, TEST_PASS, TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Success(Authentication(success = true, requestToken = null)))
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Authorized token was null", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository).login(TEST_USER, TEST_PASS, TEST_TOKEN)
    verify(mockRepository, never()).createSessionLogin(any())
  }

  @Test
  fun authorize_unexpectedState_emitsUnexpectedStateError() = runTest {
    stubTokenSuccess()
    whenever(mockRepository.login(TEST_USER, TEST_PASS, TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Loading)
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Unexpected state", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository, never()).createSessionLogin(any())
  }

  // Step 3: createSession

  @Test
  fun createSession_errorOutcome_emitsErrorAndStopsChain() = runTest {
    stubTokenSuccess()
    stubLoginSuccess()
    whenever(mockRepository.createSessionLogin(TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Error("Session creation failed"))
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Session creation failed", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository).createSessionLogin(TEST_TOKEN)
    verify(mockRepository, never()).getAccountDetails(any())
    verify(mockRepository, never()).saveUserPref(any())
  }

  @Test
  fun createSession_unsuccessful_emitsSessionCreationFailedAndStopsChain() = runTest {
    stubTokenSuccess()
    stubLoginSuccess()
    whenever(mockRepository.createSessionLogin(TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Success(CreateSession(success = false, sessionId = TEST_SESSION)))
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Session creation failed", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository, never()).getAccountDetails(any())
    verify(mockRepository, never()).saveUserPref(any())
  }

  @Test
  fun createSession_unexpectedState_emitsUnexpectedStateError() = runTest {
    stubTokenSuccess()
    stubLoginSuccess()
    whenever(mockRepository.createSessionLogin(TEST_TOKEN)).thenReturn(
      flowOf(Outcome.Loading)
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Unexpected state", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository, never()).getAccountDetails(any())
  }

  // Step 4: getAccountDetails

  @Test
  fun getAccountDetails_errorOutcome_emitsErrorAndNotSaveUser() = runTest {
    stubTokenSuccess()
    stubLoginSuccess()
    stubSessionSuccess()
    whenever(mockRepository.getAccountDetails(TEST_SESSION)).thenReturn(
      flowOf(Outcome.Error("Can't get user details"))
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Can't get user details", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository).getAccountDetails(TEST_SESSION)
    verify(mockRepository, never()).saveUserPref(any())
  }

  @Test
  fun getAccountDetails_unexpectedState_emitsUnexpectedStateError() = runTest {
    stubTokenSuccess()
    stubLoginSuccess()
    stubSessionSuccess()
    whenever(mockRepository.getAccountDetails(TEST_SESSION)).thenReturn(
      flowOf(Outcome.Loading)
    )

    interactor.login(TEST_USER, TEST_PASS).test {
      assertTrue(awaitItem() is Outcome.Loading)
      assertEquals("Unexpected state", (awaitItem() as Outcome.Error).message)
      awaitComplete()
    }

    verify(mockRepository, never()).saveUserPref(any())
  }

  // Step 5: saveUserPref edge cases

  @Test
  fun saveUserPref_nullAccountId_savesUserIdAsZero() = runTest {
    stubTokenSuccess()
    stubLoginSuccess()
    stubSessionSuccess()
    whenever(mockRepository.getAccountDetails(TEST_SESSION)).thenReturn(
      flowOf(Outcome.Success(AccountDetails(id = null, username = TEST_USER)))
    )

    interactor.login(TEST_USER, TEST_PASS).collect()

    val captor = argumentCaptor<UserModel>()
    verify(mockRepository).saveUserPref(captor.capture())
    assertEquals(0, captor.firstValue.userId)
  }

  @Test
  fun saveUserPref_nullAvatars_savesNullGravatarHashAndTmdbAvatar() = runTest {
    stubTokenSuccess()
    stubLoginSuccess()
    stubSessionSuccess()
    whenever(mockRepository.getAccountDetails(TEST_SESSION)).thenReturn(
      flowOf(
        Outcome.Success(
          AccountDetails(
            id = 1,
            username = TEST_USER,
            avatarItem = AvatarItem(gravatar = null, avatarTMDb = null)
          )
        )
      )
    )

    interactor.login(TEST_USER, TEST_PASS).collect()

    val captor = argumentCaptor<UserModel>()
    verify(mockRepository).saveUserPref(captor.capture())
    assertNull(captor.firstValue.gravatarHash)
    assertNull(captor.firstValue.tmdbAvatar)
  }

  @Test
  fun deleteSession_whenSuccessful_returnsSuccessResult() = runTest {
    val response =
      PostResult(success = true, statusCode = 200, statusMessage = "Delete session success")
    val flow = flowOf(Outcome.Success(response))
    whenever(mockRepository.deleteSession(TEST_SESSION)).thenReturn(flow)

    interactor.deleteSession(TEST_SESSION).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertTrue(emission.data.success == true)
      assertEquals(200, emission.data.statusCode)
      assertEquals("Delete session success", emission.data.statusMessage)
      awaitComplete()
    }
    verify(mockRepository, times(1)).deleteSession(TEST_SESSION)
  }

  @Test
  fun deleteSession_whenFailedOccur_returnsErrorMessage() = runTest {
    val flow = flowOf(Outcome.Error(message = ERROR_MESSAGE))
    whenever(mockRepository.deleteSession(TEST_SESSION)).thenReturn(flow)

    interactor.deleteSession(TEST_SESSION).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(ERROR_MESSAGE, emission.message)
      awaitComplete()
    }
  }
}
