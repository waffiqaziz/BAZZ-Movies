package com.waffiq.bazz_movies.core.user.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.core.user.testutils.HelperVariableTest.userModelPref
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class UserPreferenceMemTest {
  private lateinit var userPreference: UserPreference
  private lateinit var dataStore: DataStore<Preferences>

  @Before
  fun setup() {
    // use a test-specific in-memory DataStore implementation
    val testDataStoreFile = File.createTempFile("test_datastore", ".preferences_pb")
    dataStore = PreferenceDataStoreFactory.create { testDataStoreFile }
    userPreference = UserPreference(dataStore)
  }

  @Test
  fun saveUser_withAllDataAvailable_storesCorrectData() = runTest {
    val severalData = UserModelPref(
      userId = 123456789,
      name = "John Doe",
      username = "johndoe",
      password = "password123",
      region = "US",
      token = "sampleToken",
      isLogin = true,
      gravatarHast = null,
      tmdbAvatar = null
    )
    userPreference.saveUser(severalData)

    val savedUser = userPreference.getUser().first()
    assertEquals(123456789, savedUser.userId)
    assertEquals("John Doe", savedUser.name)
    assertEquals("johndoe", savedUser.username)
    assertEquals("password123", savedUser.password)
    assertEquals("US", savedUser.region)
    assertEquals("sampleToken", savedUser.token)
    assertTrue(savedUser.isLogin)
    assertEquals("", savedUser.gravatarHast)
    assertEquals("", savedUser.tmdbAvatar)
  }

  @Test
  fun saveUser_withPartialDataAvailable_storesCorrectData() = runTest {
    userPreference.saveUser(userModelPref)

    val savedUser = userPreference.getUser().first()
    assertEquals(123456789, savedUser.userId)
    assertEquals("John Doe", savedUser.name)
    assertEquals("johndoe", savedUser.username)
    assertEquals("password123", savedUser.password)
    assertEquals("US", savedUser.region)
    assertEquals("sampleToken", savedUser.token)
    assertTrue(savedUser.isLogin)
    assertEquals("hash123", savedUser.gravatarHast)
    assertEquals("avatar.jpg", savedUser.tmdbAvatar)
  }

  @Test
  fun saveRegion_updatesRegionCorrectly() = runTest {
    userPreference.saveUser(userModelPref)

    val savedUser = userPreference.getUser().first()
    assertEquals(123456789, savedUser.userId)
    assertEquals("John Doe", savedUser.name)

    userPreference.saveRegion("ID")
    val updatedRegion = userPreference.getRegion().first()
    assertEquals("ID", updatedRegion)
  }

  @Test
  fun removeUserData_clearsAllDataCorrectly() = runTest {
    userPreference.saveUser(userModelPref)

    val savedUser = userPreference.getUser().first()
    assertEquals(123456789, savedUser.userId)
    assertEquals("John Doe", savedUser.name)

    userPreference.removeUserData()

    val clearedUser = userPreference.getUser().first()
    assertEquals(0, clearedUser.userId)
    assertEquals("", clearedUser.name)
    assertEquals("", clearedUser.username)
    assertEquals("", clearedUser.password)
    assertEquals("", clearedUser.region)
    assertEquals("", clearedUser.token)
    assertFalse(clearedUser.isLogin)
    assertEquals("", clearedUser.gravatarHast)
    assertEquals("", clearedUser.tmdbAvatar)
  }
}
