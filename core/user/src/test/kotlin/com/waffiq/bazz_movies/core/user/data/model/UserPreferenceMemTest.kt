package com.waffiq.bazz_movies.core.user.data.model

import com.waffiq.bazz_movies.core.user.testutils.DataStoreTestRule
import com.waffiq.bazz_movies.core.user.testutils.HelperVariableTest.userModelPref
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserPreferenceMemTest {

  @get:Rule
  val dataStoreRule = DataStoreTestRule()

  private lateinit var userPreference: UserPreference

  @Before
  fun setup() {
    userPreference = UserPreference(dataStoreRule.testDataStore)
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
      gravatarHash = null,
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
    assertEquals("", savedUser.gravatarHash)
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
    assertEquals("hash123", savedUser.gravatarHash)
    assertEquals("avatar.jpg", savedUser.tmdbAvatar)
  }

  @Test
  fun saveRegion_whenSuccessful_updatesRegionCorrectly() = runTest {
    userPreference.saveUser(userModelPref)

    val savedUser = userPreference.getUser().first()
    assertEquals(123456789, savedUser.userId)
    assertEquals("John Doe", savedUser.name)

    userPreference.saveRegion("ID")
    val updatedRegion = userPreference.getRegion().first()
    assertEquals("ID", updatedRegion)
  }

  @Test
  fun removeUserData_whenSuccessful_clearsAllDataCorrectly() = runTest {
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
    assertEquals("", clearedUser.gravatarHash)
    assertEquals("", clearedUser.tmdbAvatar)
  }
}
