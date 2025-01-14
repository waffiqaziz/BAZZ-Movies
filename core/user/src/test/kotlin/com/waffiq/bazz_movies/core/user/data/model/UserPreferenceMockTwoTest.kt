package com.waffiq.bazz_movies.core.user.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.mutablePreferencesOf
import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserPreferenceMockTwoTest {

  private lateinit var userPreference: UserPreference
  private lateinit var mockDataStore: DataStore<Preferences>
  private lateinit var mockPreferences: Preferences

  @Before
  fun setup() = runTest {
    mockDataStore = mock<DataStore<Preferences>>()
    mockPreferences = mock<Preferences>()

    // Mock dataStore.data to emit an initial preference (region is "US")
    val initialPreferencesFlow = flowOf(mutablePreferencesOf(UserPreference.REGION_KEY to "US"))
    `when`(mockDataStore.data).thenReturn(initialPreferencesFlow)

    // Mock the edit function to update preferences when saveRegion is called
    `when`(mockDataStore.edit(any())).thenAnswer { invocation ->
      // Capture the block passed to edit()
      val block = invocation.getArgument<suspend (MutablePreferences) -> Unit>(0)

      // Create a mutable preferences map with initial "US" region
      val mutablePreferences = mutablePreferencesOf(UserPreference.REGION_KEY to "US")

      // Run the block and apply the region change
      runBlocking { block(mutablePreferences) }

      // Modify the region to "ID" inside the block
      mutablePreferences[UserPreference.REGION_KEY] = "ID"

      // Return the modified preferences
      mutablePreferences
    }

    userPreference = UserPreference(mockDataStore)
  }

  @Test
  fun `saveRegion should save the correct region`() = runTest {
    // Call saveRegion to update region to "ID"
    userPreference.saveRegion("ID")

    // Mock dataStore.data to emit updated preferences
    val updatedPreferencesFlow = flowOf(mutablePreferencesOf(UserPreference.REGION_KEY to "ID"))
    `when`(mockDataStore.data).thenReturn(updatedPreferencesFlow)

    // Verify that getRegion() emits the correct updated region
    val savedRegion = userPreference.getRegion().first() // This should return "ID"
    assertEquals("ID", savedRegion)

    // Verify that edit was called with the correct key
    verify(mockDataStore).edit(any())
  }

  @Test
  fun `saveRegion should update region when key exists`() = runTest {
    userPreference.saveRegion("US")
    `when`(mockDataStore.data).thenReturn(flowOf(mutablePreferencesOf(UserPreference.REGION_KEY to "US")))
    val firstRegion = userPreference.getRegion().first()
    assertEquals("US", firstRegion)

    userPreference.saveRegion("ID")
    `when`(mockDataStore.data).thenReturn(flowOf(mutablePreferencesOf(UserPreference.REGION_KEY to "ID")))

    verify(mockDataStore, times(2)).edit(any())

    val updatedRegion = userPreference.getRegion().first()
    assertEquals("ID", updatedRegion)
  }
}
