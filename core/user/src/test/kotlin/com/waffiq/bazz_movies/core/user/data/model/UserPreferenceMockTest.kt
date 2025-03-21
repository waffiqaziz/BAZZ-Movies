package com.waffiq.bazz_movies.core.user.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserPreferenceMockTest {

  private lateinit var userPreference: UserPreference
  private lateinit var mockDataStore: DataStore<Preferences>
  private lateinit var mockPreferences: Preferences

  @Before
  fun setup() {
    mockDataStore = mock() // Mock DataStore
    userPreference = UserPreference(mockDataStore) // Inject mock DataStore

    mockPreferences = mock<Preferences>()
    `when`(mockPreferences[UserPreference.USERID_KEY]).thenReturn(123)
    `when`(mockPreferences[UserPreference.NAME_KEY]).thenReturn("John Doe")
    `when`(mockPreferences[UserPreference.USERNAME_KEY]).thenReturn("johndoe")
    `when`(mockPreferences[UserPreference.PASSWORD_KEY]).thenReturn("password123")
    `when`(mockPreferences[UserPreference.REGION_KEY]).thenReturn("US")
    `when`(mockPreferences[UserPreference.TOKEN_KEY]).thenReturn("sampleToken")
    `when`(mockPreferences[UserPreference.STATE_KEY]).thenReturn(true)
    `when`(mockPreferences[UserPreference.GRAVATAR_KEY]).thenReturn("hash123")
    `when`(mockPreferences[UserPreference.TMDB_AVATAR_KEY]).thenReturn("avatar.jpg")
  }

  @Test
  fun getUser_returnsCorrectUserModel() = runTest {
    `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences))

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
  fun getToken_returnsCorrectToken() = runTest {
    `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences))

    val token = userPreference.getToken().first()
    assertEquals("sampleToken", token)
    verify(mockDataStore, times(1)).data

    val listOfToken = userPreference.getToken().toList()

    assertEquals(listOf("sampleToken"), listOfToken)

    // inline test
    userPreference.getToken().test {
      val resultToken = awaitItem()
      assertEquals("sampleToken", resultToken)
      cancelAndIgnoreRemainingEvents()
    }
    verify(mockDataStore, times(3)).data
  }

  @Test
  fun getRegion_returnsCorrectRegion() = runTest {
    `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences))

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
  fun getRegion_handlesNullRegion() = runTest {
    `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences)) // explicitly mock preferences object

    // Inline test with Turbine
    userPreference.getRegion().test {
      val result = awaitItem()
      assertEquals("US", result)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun saveRegion_edit_callsEdit() = runTest {
    `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences)) // explicitly mock preferences object
    `when`(mockDataStore.edit(any())).thenAnswer {
      // Simulate that edit does nothing (since it returns Unit)
      return@thenAnswer Unit
    }

    // Inline test with Turbine
    userPreference.saveRegion("US")
    verify(mockDataStore).edit(any())
  }
}
