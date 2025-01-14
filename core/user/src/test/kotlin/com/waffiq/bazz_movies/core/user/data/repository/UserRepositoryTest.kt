package com.waffiq.bazz_movies.core.user.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.network.data.remote.datasource.UserDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.countryip.CountryIPResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AccountDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AuthenticationResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.AvatarTMDbResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.CreateSessionResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account.GravatarResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.user.data.model.UserModelPref
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import com.waffiq.bazz_movies.core.user.testutils.TestHelper.testOutcome
import com.waffiq.bazz_movies.core.user.testutils.TestHelper.testResult
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAccountDetails
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toAuthentication
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCountryIP
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toCreateSession
import com.waffiq.bazz_movies.core.user.utils.mappers.AccountMapper.toUserModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserRepositoryTest {
  private val sessionId = "session_id"
  private val user = UserModel(
    userId = 123456789,
    name = "John Doe",
    username = "johndoe",
    password = "password123",
    region = "US",
    token = "sampleToken",
    isLogin = true,
    gravatarHast = "hash123",
    tmdbAvatar = "avatar.jpg"
  )
  private val userModelPref = UserModelPref(
    userId = 123456789,
    name = "John Doe",
    username = "johndoe",
    password = "password123",
    region = "US",
    token = "sampleToken",
    isLogin = true,
    gravatarHast = "hash123",
    tmdbAvatar = "avatar.jpg"
  )

  private lateinit var userRepository: UserRepository
  private val mockUserPreference: UserPreference = mockk()
  private val mockUserDataSource: UserDataSource = mockk()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    userRepository = UserRepository(mockUserPreference, mockUserDataSource)
  }

  @Test
  fun `createToken success should return mapped result`() = runTest {
    val createTokenResponse = AuthenticationResponse(
      success = true,
      expireAt = "date_expired",
      requestToken = "request_token"
    )

    // Arrange
    val flowResult = flowOf(NetworkResult.Success(createTokenResponse))
    coEvery { mockUserDataSource.createToken() } returns flowResult

    // Act
    val result = userRepository.createToken().first()

    // Assert
    assertTrue(result is Outcome.Success)
    result as Outcome.Success
    assertEquals(true, result.data.success)
    assertEquals("request_token", result.data.requestToken)
    assertEquals("date_expired", result.data.expireAt)
    coVerify { mockUserDataSource.createToken() }

    // inline test
    userRepository.createToken().testOutcome(
      expectedData = createTokenResponse.toAuthentication()
    )
  }

  @Test
  fun `login success should return mapped authentication`() = runTest {
    val loginResponse = AuthenticationResponse(
      success = true,
      expireAt = "date_expired",
      requestToken = "request_token_verified"
    )

    val username = "test_user"
    val password = "password123"
    val token = "request_token"

    val flowResult = flowOf(NetworkResult.Success(loginResponse))
    coEvery { mockUserDataSource.login(username, password, token) } returns flowResult

    val result = userRepository.login(username, password, token).first()

    assertTrue(result is Outcome.Success)
    result as Outcome.Success
    assertEquals("request_token_verified", result.data.requestToken)
    coVerify { mockUserDataSource.login(username, password, token) }

    // inline test
    userRepository.login(username, password, token).testOutcome(
      expectedData = loginResponse.toAuthentication()
    )
  }

  @Test
  fun `create session login success should return mapped session`() = runTest {
    val sessionResponse = CreateSessionResponse(
      success = true,
      sessionId = "session_id"
    )

    val requestToken = "request_token"

    val flowResult = flowOf(NetworkResult.Success(sessionResponse))
    coEvery { mockUserDataSource.createSessionLogin(requestToken) } returns flowResult

    val result = userRepository.createSessionLogin(requestToken).first()

    assertTrue(result is Outcome.Success)
    result as Outcome.Success
    assertTrue(result.data.success)
    assertEquals("session_id", result.data.sessionId)
    coVerify { mockUserDataSource.createSessionLogin(requestToken) }

    // inline test
    userRepository.createSessionLogin(requestToken).testOutcome(
      expectedData = sessionResponse.toCreateSession()
    )
  }

  @Test
  fun `delete session success should return success`() = runTest {
    val deleteSessionResponse = PostResponse(
      success = true,
      statusCode = 200,
      statusMessage = "Success"
    )

    val flowResult = flowOf(NetworkResult.Success(deleteSessionResponse))
    coEvery { mockUserDataSource.deleteSession(sessionId) } returns flowResult

    val result = userRepository.deleteSession(sessionId).first()

    assertTrue(result is Outcome.Success)
    result as Outcome.Success
    assertEquals(true, result.data.success)
    assertEquals(200, result.data.statusCode)
    assertEquals("Success", result.data.statusMessage)
    coVerify { mockUserDataSource.deleteSession(sessionId) }

    // inline test
    userRepository.deleteSession(sessionId).testOutcome(
      expectedData = deleteSessionResponse.toPost()
    )
  }

  @Test
  fun `delete session failed should return error message`() = runTest {
    val failedResponse = PostResponse(
      success = false,
      statusCode = 11,
      statusMessage = "Internal error: Something went wrong, contact TMDb."
    )

    val flowResult = flowOf(NetworkResult.Success(failedResponse))
    coEvery { mockUserDataSource.deleteSession(sessionId) } returns flowResult

    val result = userRepository.deleteSession(sessionId).first()

    assertTrue(result is Outcome.Success)
    result as Outcome.Success
    assertEquals(false, result.data.success)
    assertEquals(11, result.data.statusCode)
    assertEquals("Internal error: Something went wrong, contact TMDb.", result.data.statusMessage)
    coVerify { mockUserDataSource.deleteSession(sessionId) }
  }

  @Test
  fun `get detail user should return mapped user detail`() = runTest {
    val accountDetailsResponse = AccountDetailsResponse(
      includeAdult = false,
      iso31661 = "en",
      name = "Waffiq",
      avatarItemResponse = AvatarItemResponse(
        gravatarResponse = GravatarResponse(
          hash = "325987423659432"
        ),
        avatarTMDbResponse = AvatarTMDbResponse(
          avatarPath = "347589074283054"
        )
      ),
      id = 513176325,
      iso6391 = "ID",
      username = "waffiq1234",
    )

    val sessionId = "32154325425662"

    val flowResult = flowOf(NetworkResult.Success(accountDetailsResponse))
    coEvery { mockUserDataSource.getUserDetail(sessionId) } returns flowResult

    val result = userRepository.getUserDetail(sessionId).first()

    assertTrue(result is Outcome.Success)
    result as Outcome.Success
    assertEquals(513176325, result.data.id)
    assertEquals("ID", result.data.iso6391)
    assertEquals("en", result.data.iso31661)
    assertEquals("Waffiq", result.data.name)
    assertEquals("waffiq1234", result.data.username)
    assertEquals("347589074283054", result.data.avatarItem?.avatarTMDb?.avatarPath)
    assertEquals("325987423659432", result.data.avatarItem?.gravatar?.hash)
    assertFalse(result.data.includeAdult == true)
    coVerify { mockUserDataSource.getUserDetail(sessionId) }

    // inline test
    userRepository.getUserDetail(sessionId).testOutcome(
      expectedData = accountDetailsResponse.toAccountDetails()
    )
  }

  @Test
  fun `createToken error should return error message correctly`() = runTest {
    val errorMessage = "Network error"
    val errorFlow = flowOf(NetworkResult.Error(errorMessage))
    coEvery { mockUserDataSource.createToken() } returns errorFlow

    val result = userRepository.createToken().first()
    assertTrue(result is Outcome.Error)
    result as Outcome.Error
    assertEquals(errorMessage, result.message)
    coVerify { mockUserDataSource.createToken() }
  }

  @Test
  fun `saveUserPref should invoke UserPreference saveUser`() = runTest {
    coEvery { mockUserPreference.saveUser(userModelPref) } just Runs
    userRepository.saveUserPref(user)
    coVerify { mockUserPreference.saveUser(userModelPref) }
  }

  @Test
  fun `saveRegionPref should invoke UserPreference saveRegion`() = runTest {
    coEvery { mockUserPreference.saveRegion("ID") } just Runs
    userRepository.saveRegionPref("ID")
    coVerify { mockUserPreference.saveRegion("ID") }
  }

  @Test
  fun `getUserPref should emit user model`() = runTest {
    val flowResult = flowOf(userModelPref)
    every { mockUserPreference.getUser() } returns flowResult

    val resultPref = mockUserPreference.getUser().map { it.toUserModel() }.first()
    val resultRepo = userRepository.getUserPref().first()
    assertEquals(resultPref, resultRepo)

    val resultsList = userRepository.getUserPref().toList()
    assertEquals(listOf(userModelPref.toUserModel()), resultsList)

    assertEquals(user, resultPref)
    verify { mockUserPreference.getUser() }

    // inline test
    userRepository.getUserPref().testResult(userModelPref.toUserModel())
    verify { mockUserPreference.getUser() }
  }

  @Test
  fun `getUserToken should emit user token`() = runTest {
    val token = "token"
    val flowResult = flowOf(token)
    every { mockUserPreference.getToken() } returns flowResult

    val resultToken = userRepository.getUserToken().first()
    assertEquals(token, resultToken)
    verify { mockUserPreference.getToken() }

    // inline test
    userRepository.getUserToken().testResult(expectedData = "token")
  }

  @Test
  fun `getUserRegionPref should emit user region`() = runTest {
    val region = "ID"
    val flowResult = flowOf(region)
    every { mockUserPreference.getRegion() } returns flowResult

    val resultRegion = userRepository.getUserRegionPref().first()
    assertEquals(region, resultRegion)
    verify { mockUserPreference.getRegion() }

    // inline test
    userRepository.getUserRegionPref().testResult(expectedData = "ID")
  }

  @Test
  fun `removeUserDataPref should call removeUserData in UserPreference`() = runTest {
    coEvery { mockUserPreference.removeUserData() } just Runs
    userRepository.removeUserDataPref()
    coVerify { mockUserPreference.removeUserData() }
  }

  @Test
  fun `getCountryCode should return mapped country code`() = runTest {
    val countryIPResponse = CountryIPResponse(
      country = "ID"
    )

    val flowResult = flowOf(NetworkResult.Success(countryIPResponse))
    coEvery { mockUserDataSource.getCountryCode() } returns flowResult

    val result = userRepository.getCountryCode().first()

    assertTrue(result is Outcome.Success)
    result as Outcome.Success
    assertEquals("ID", result.data.country)
    coVerify { mockUserDataSource.getCountryCode() }

    // inline test
    userRepository.getCountryCode().testOutcome(
      expectedData = countryIPResponse.toCountryIP()
    )
  }
}
