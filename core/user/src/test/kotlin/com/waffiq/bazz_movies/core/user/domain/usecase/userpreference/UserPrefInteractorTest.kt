package com.waffiq.bazz_movies.core.user.domain.usecase.userpreference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserPrefInteractorTest {

  // Mock repository
  private val mockRepository: IUserRepository = mockk()
  private lateinit var userPrefInteractor: UserPrefInteractor

  // Mock the DataStore
  private val mockDataStore: DataStore<Preferences> = mockk(relaxed = true)
  private lateinit var userPreference: UserPreference

  @Before
  fun setup() {
    userPrefInteractor = UserPrefInteractor(mockRepository)
    userPreference = UserPreference(mockDataStore)
  }

  @Test
  fun `getUserPref emits user data`() = runTest {
    val userModel = UserModel(
      userId = 3,
      name = "Alice",
      username = "alice123",
      password = "",
      region = "UK",
      token = "token123",
      isLogin = true,
      gravatarHast = "hash123",
      tmdbAvatar = "https://example.com/avatar2.jpg"
    )
    val flow = flowOf(userModel)
    every { mockRepository.getUserPref() } returns flow

    userPrefInteractor.getUser().test {
      // Assert the first emission is success with correct data
      val emission = awaitItem()
      assertEquals(3, emission.userId)
      assertEquals("Alice", emission.name)
      assertEquals("alice123", emission.username)
      assertEquals("UK", emission.region)
      assertEquals("token123", emission.token)
      assertTrue("alice123", emission.isLogin)
      assertEquals("hash123", emission.gravatarHast)
      assertEquals("https://example.com/avatar2.jpg", emission.tmdbAvatar)

      // Assert no further emissions
      awaitComplete()
    }

    // Verify repository interaction
    verify(exactly = 1) { mockRepository.getUserPref() }
    verify { mockRepository.getUserPref() }
  }

  @Test
  fun `getUserRegionPref emits user region`() = runTest {
    val region = "ID"
    val flow = flowOf(region)
    every { mockRepository.getUserRegionPref() } returns flow
    userPrefInteractor.getUserRegionPref().test {
      val emission = awaitItem()
      assertEquals("ID", emission)
      awaitComplete()
    }
    verify { mockRepository.getUserRegionPref() }
  }

  @Test
  fun `getUserToken emits user token`() = runTest {
    val userToken = "user_token"
    val flow = flowOf(userToken)
    every { mockRepository.getUserToken() } returns flow
    userPrefInteractor.getUserToken().test {
      val emission = awaitItem()
      assertEquals("user_token", emission)
      awaitComplete()
    }
    verify { mockRepository.getUserToken() }
  }

  // TODO : Create other tests for `saveRegionPref`, `saveUserPref`, and `removeUserDataPref` for `UserPrefInteractor`
}
