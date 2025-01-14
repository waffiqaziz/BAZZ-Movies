package com.waffiq.bazz_movies.core.user.domain.usecase.authtmdbaccount

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.user.domain.model.account.AccountDetails
import com.waffiq.bazz_movies.core.user.domain.model.account.Authentication
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarItem
import com.waffiq.bazz_movies.core.user.domain.model.account.AvatarTMDb
import com.waffiq.bazz_movies.core.user.domain.model.account.CreateSession
import com.waffiq.bazz_movies.core.user.domain.model.account.Gravatar
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Test AuthTMDbAccountInteractor using mockito
 */
class AuthTMDbAccountInteractorTest {
  private val sessionId = "session_id"
  private val errorMessage = "Network error"
  private val requestToken = "request_token"

  private val mockRepository: IUserRepository = mock()
  private lateinit var authTMDbAccountInteractor: AuthTMDbAccountInteractor

  @Before
  fun setup() {
    authTMDbAccountInteractor = AuthTMDbAccountInteractor(mockRepository)
  }

  @Test
  fun login_SuccessReturnsSuccessResult() = runTest {
    val response = Authentication(
      success = true,
      expireAt = "date_expire",
      requestToken = "request_token_verified"
    )
    val username = "username"
    val pass = "password"

    val flow = flowOf(Outcome.Success(response))
    whenever(mockRepository.login(username, pass, requestToken)).thenReturn(flow)

    authTMDbAccountInteractor.login(username, pass, requestToken).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertTrue(emission.data.success)
      assertEquals("date_expire", emission.data.expireAt)
      assertEquals("request_token_verified", emission.data.requestToken)
      awaitComplete()
    }
    verify(mockRepository, times(1)).login(username, pass, requestToken)
  }

  @Test
  fun login_FailedReturnsErrorMessage() = runTest {
    val username = "username"
    val pass = "password"
    val sessionId = "session_id"

    val flow = flowOf(Outcome.Error(message = errorMessage))
    whenever(mockRepository.login(username, pass, sessionId)).thenReturn(flow)

    authTMDbAccountInteractor.login(username, pass, sessionId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    verify(mockRepository, times(1)).login(username, pass, sessionId)
  }

  @Test
  fun createToken_SuccessReturnsSuccessResult() = runTest {
    val response =
      Authentication(success = true, expireAt = "date_expire", requestToken = "request_token")

    val flow = flowOf(Outcome.Success(response))
    whenever(mockRepository.createToken()).thenReturn(flow)

    authTMDbAccountInteractor.createToken().test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertTrue(emission.data.success)
      assertEquals("date_expire", emission.data.expireAt)
      assertEquals("request_token", emission.data.requestToken)
      awaitComplete()
    }
    verify(mockRepository, times(1)).createToken()
  }

  @Test
  fun createToken_FailedReturnsErrorMessage() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    whenever(mockRepository.createToken()).thenReturn(flow)

    authTMDbAccountInteractor.createToken().test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    verify(mockRepository, times(1)).createToken()
  }

  @Test
  fun deleteSession_SuccessReturnsSuccessResult() = runTest {
    val response = Post(success = true, statusCode = 200, statusMessage = "Delete session success")
    val flow = flowOf(Outcome.Success(response))
    whenever(mockRepository.deleteSession(sessionId)).thenReturn(flow)

    authTMDbAccountInteractor.deleteSession(sessionId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertTrue(emission.data.success == true)
      assertEquals(200, emission.data.statusCode)
      assertEquals("Delete session success", emission.data.statusMessage)
      awaitComplete()
    }
    verify(mockRepository, times(1)).deleteSession(sessionId)
  }

  @Test
  fun deleteSession_FailedReturnsErrorMessage() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    whenever(mockRepository.deleteSession(sessionId)).thenReturn(flow)

    authTMDbAccountInteractor.deleteSession(sessionId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
  }

  @Test
  fun createSession_LoginSuccessReturnsSuccessResult() = runTest {
    val response = CreateSession(success = true, sessionId = "session_id")

    val flow = flowOf(Outcome.Success(response))
    whenever(mockRepository.createSessionLogin(requestToken)).thenReturn(flow)

    authTMDbAccountInteractor.createSessionLogin(requestToken).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertTrue(emission.data.success)
      assertEquals("session_id", emission.data.sessionId)
      awaitComplete()
    }
  }

  @Test
  fun createSession_LoginFailedReturnsErrorMessage() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    whenever(mockRepository.createSessionLogin(requestToken)).thenReturn(flow)

    authTMDbAccountInteractor.createSessionLogin(requestToken).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
  }

  @Test
  fun getUserDetails_SuccessReturnsSuccessResult() = runTest {
    val response = AccountDetails(
      includeAdult = false,
      iso31661 = "en",
      name = "Waffiq",
      avatarItem = AvatarItem(
        gravatar = Gravatar(
          hash = "325987423659432"
        ),
        avatarTMDb = AvatarTMDb(
          avatarPath = "347589074283054"
        )
      ),
      id = 12345678,
      iso6391 = "ID",
      username = "waffiq12345",
    )

    val flow = flowOf(Outcome.Success(response))
    whenever(mockRepository.getUserDetail(sessionId)).thenReturn(flow)

    authTMDbAccountInteractor.getUserDetail(sessionId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertFalse(emission.data.includeAdult == true)
      assertEquals("en", emission.data.iso31661)
      assertEquals("Waffiq", emission.data.name)
      assertEquals("325987423659432", emission.data.avatarItem?.gravatar?.hash)
      assertEquals("347589074283054", emission.data.avatarItem?.avatarTMDb?.avatarPath)
      assertEquals(12345678, emission.data.id)
      assertEquals("ID", emission.data.iso6391)
      assertEquals("waffiq12345", emission.data.username)
      awaitComplete()
    }
  }

  @Test
  fun getUserDetails_FailedReturnsErrorMessage() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    whenever(mockRepository.getUserDetail(sessionId)).thenReturn(flow)

    authTMDbAccountInteractor.getUserDetail(sessionId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
  }
}
