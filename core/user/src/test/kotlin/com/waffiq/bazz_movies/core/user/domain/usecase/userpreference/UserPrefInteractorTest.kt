package com.waffiq.bazz_movies.core.user.domain.usecase.userpreference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserPrefInteractorTest {

  // mock repository
  private val mockRepository: IUserRepository = mockk()
  private lateinit var userPrefInteractor: UserPrefInteractor

  // mock the DataStore
  private val mockDataStore: DataStore<Preferences> = mockk(relaxed = true)
  private lateinit var userPreference: UserPreference

  @Before
  fun setup() {
    userPrefInteractor = UserPrefInteractor(mockRepository)
    userPreference = UserPreference(mockDataStore)
  }

  @Test
  fun getUserPref_whenSuccessful_emitsUserData() = runTest {
    val userModel = UserModel(
      userId = 3,
      name = "Alice",
      username = "alice123",
      password = "",
      region = "UK",
      token = "token123",
      isLogin = true,
      gravatarHash = "hash123",
      tmdbAvatar = "https://example.com/avatar2.jpg"
    )
    val flow = flowOf(userModel)
    every { mockRepository.getUserPref() } returns flow

    userPrefInteractor.getUser().test {
      // assert the first emission is success with correct data
      val emission = awaitItem()
      assertEquals(3, emission.userId)
      assertEquals("Alice", emission.name)
      assertEquals("alice123", emission.username)
      assertEquals("UK", emission.region)
      assertEquals("token123", emission.token)
      assertTrue("alice123", emission.isLogin)
      assertEquals("hash123", emission.gravatarHash)
      assertEquals("https://example.com/avatar2.jpg", emission.tmdbAvatar)

      // assert no further emissions
      awaitComplete()
    }

    // verify repository interaction
    verify(exactly = 1) { mockRepository.getUserPref() }
    verify { mockRepository.getUserPref() }
  }

  @Test
  fun getUserRegionPref_whenSuccessful_emitsUserRegion() = runTest {
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
  fun getUserToken_whenSuccessful_emitsUserToken() = runTest {
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

  @Test
  fun getPermissionAsked_whenSuccessful_emitsUserToken() = runTest {
    every { mockRepository.getPermissionAsked() } returns flowOf(true)
    userPrefInteractor.getPermissionAsked().test {
      val emission = awaitItem()
      assertTrue(emission)
      awaitComplete()
    }
    verify { mockRepository.getPermissionAsked() }
  }

  @Test
  fun saveRegionPref_whenCalled_callsSaveRegionPrefFromRepository() = runTest {
    val region = "ID"
    coEvery { mockRepository.saveRegionPref(region) } just Runs
    userPrefInteractor.saveRegionPref(region)
    coVerify { mockRepository.saveRegionPref(region) }
  }

  @Test
  fun saveUserPref_whenSuccessful_callsSaveUserPrefFromRepository() = runTest {
    val userModel = UserModel(
      userId = 3,
      name = "Alice",
      username = "alice123",
      password = "",
      region = "UK",
      token = "token123",
      isLogin = true,
      gravatarHash = "hash123",
      tmdbAvatar = "https://example.com/avatar2.jpg"
    )
    coEvery { mockRepository.saveUserPref(userModel) } just Runs
    userPrefInteractor.saveUserPref(userModel)
    coVerify { mockRepository.saveUserPref(userModel) }
  }

  @Test
  fun savePermissionAsked_whenSuccessful_callsSavesPermissionAskedFromRepository() = runTest {
    coEvery { mockRepository.savePermissionAsked() } just Runs
    userPrefInteractor.savePermissionAsked()
    coVerify { mockRepository.savePermissionAsked() }
  }

  @Test
  fun removeUserDataPref_whenCalled_callsRemoveUserDataPrefFromRepository() = runTest {
    coEvery { mockRepository.removeUserDataPref() } just Runs
    userPrefInteractor.removeUserDataPref()
    coVerify { mockRepository.removeUserDataPref() }
  }
}
