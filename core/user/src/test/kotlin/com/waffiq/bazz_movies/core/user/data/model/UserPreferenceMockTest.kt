package com.waffiq.bazz_movies.core.user.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class UserPreferenceMockTest {

  private lateinit var userPreference: UserPreference
  private lateinit var mockDataStore: DataStore<Preferences>
  private lateinit var mockPreferences :Preferences

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
  fun `test getUser returns correct UserModel`() = runTest {
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
  }

  @Test
  fun `test getToken returns correct token`() = runTest {
    `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences))

    `when`(mockPreferences[UserPreference.TOKEN_KEY]).thenReturn("sampleToken")

    val token = userPreference.getToken().first()
    assertEquals("sampleToken", token)
  }

  @Test
  fun `test getRegion returns correct region`() = runTest {
    `when`(mockDataStore.data).thenReturn(flowOf(mockPreferences))

    `when`(mockPreferences[UserPreference.REGION_KEY]).thenReturn("US")

    val region = userPreference.getRegion().first()
    assertEquals("US", region)
  }
}
