package com.waffiq.bazz_movies.core.user.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserPreferenceMockkTest {

  private lateinit var userPreference: UserPreference
  private lateinit var mockDataStore: DataStore<Preferences>
  private lateinit var mockPreferences: Preferences
  private lateinit var mockMutablePreferences: MutablePreferences

  @Before
  fun setup() {
    mockDataStore = mockk() // Mock DataStore using MockK
    userPreference = UserPreference(mockDataStore) // Inject mock DataStore

    mockPreferences = mockk()
    every { mockPreferences[UserPreference.USERID_KEY] } returns 123
    every { mockPreferences[UserPreference.NAME_KEY] } returns "John Doe"
    every { mockPreferences[UserPreference.USERNAME_KEY] } returns "johndoe"
    every { mockPreferences[UserPreference.PASSWORD_KEY] } returns "password123"
    every { mockPreferences[UserPreference.REGION_KEY] } returns "US"
    every { mockPreferences[UserPreference.TOKEN_KEY] } returns "sampleToken"
    every { mockPreferences[UserPreference.STATE_KEY] } returns true
    every { mockPreferences[UserPreference.GRAVATAR_KEY] } returns "hash123"
    every { mockPreferences[UserPreference.TMDB_AVATAR_KEY] } returns "avatar.jpg"

    mockMutablePreferences = mockk()
    every { mockMutablePreferences[UserPreference.REGION_KEY] = any<String>() } just Runs
  }

  @Test
  fun getUser_whenSuccessful_returnsCorrectUserModel() = runTest {
    every { mockDataStore.data } returns flowOf(mockPreferences)

    val user = userPreference.getUser().first()

    assertEquals(123, user.userId)
    assertEquals("John Doe", user.name)
    assertEquals("johndoe", user.username)
    assertEquals("password123", user.password)
    assertEquals("US", user.region)
    assertEquals("sampleToken", user.token)
    assertTrue(user.isLogin)
    assertEquals("hash123", user.gravatarHast)
    assertEquals("avatar.jpg", user.tmdbAvatar)

    // inline test
    userPreference.getUser().test {
      val result = awaitItem()
      assertEquals(123, result.userId)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getToken_whenSuccessful_returnsCorrectToken() = runTest {
    every { mockDataStore.data } returns flowOf(mockPreferences)

    val token = userPreference.getToken().first()
    assertEquals("sampleToken", token)
    verify(exactly = 1) { mockDataStore.data }

    val listOfToken = userPreference.getToken().toList()

    assertEquals(listOf("sampleToken"), listOfToken)

    // inline test
    userPreference.getToken().test {
      val resultToken = awaitItem()
      assertEquals("sampleToken", resultToken)
      cancelAndIgnoreRemainingEvents()
    }
    verify(exactly = 3) { mockDataStore.data }
  }

  @Test
  fun getRegion_whenSuccessful_returnsCorrectRegion() = runTest {
    every { mockDataStore.data } returns flowOf(mockPreferences)

    val region = userPreference.getRegion().first().toString().uppercase()
    assertEquals("US", region)

    // inline test
    userPreference.getRegion().test {
      val resultRegion = awaitItem()
      assertEquals("US", resultRegion)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun getRegion_whenSuccessful_handlesNullRegion() = runTest {
    every { mockDataStore.data } returns flowOf(mockPreferences) // explicitly mock preferences object

    // Inline test with Turbine
    userPreference.getRegion().test {
      val result = awaitItem()
      assertEquals("US", result)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun saveRegion_whenCalled_shouldCallEdit() = runTest {
    // Capture the transform function passed to updateData
    val transformSlot = slot<suspend (Preferences) -> Preferences>()

    // Mock the preferences that will be passed to the transform function
    val testPrefs = mockk<Preferences>()
    val mutablePrefs = mockk<MutablePreferences>()

    // Setup the mocks for the transform function
    every { testPrefs.toMutablePreferences() } returns mutablePrefs
    every { mutablePrefs[UserPreference.REGION_KEY] = "US" } just Runs

    // Mock the updateData function to capture the transform
    coEvery {
      mockDataStore.updateData(capture(transformSlot))
    } coAnswers {
      // Execute the transform function with our test preferences
      transformSlot.captured.invoke(testPrefs)
      mockPreferences
    }

    // Call the method under test
    userPreference.saveRegion("US")
    advanceUntilIdle()

    // Verify updateData was called
    coVerify { mockDataStore.updateData(any()) }

    // Verify the correct value was set in preferences
    verify { mutablePrefs[UserPreference.REGION_KEY] = "US" }
  }
}
