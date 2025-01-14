package com.waffiq.bazz_movies.core.user.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.mutablePreferencesOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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

    // mock dataStore.data to emit an initial preference (region is "US")
    val initialPreferencesFlow = flowOf(mutablePreferencesOf(UserPreference.REGION_KEY to "US"))
    `when`(mockDataStore.data).thenReturn(initialPreferencesFlow)

    userPreference = UserPreference(mockDataStore)
  }

  @Test
  fun saveRegion_savesCorrectRegion() = runTest {
    userPreference.saveRegion("ID")

    val updatedPreferencesFlow = flowOf(mutablePreferencesOf(UserPreference.REGION_KEY to "ID"))
    `when`(mockDataStore.data).thenReturn(updatedPreferencesFlow)

    val savedRegion = userPreference.getRegion().first() // This should return "ID"
    assertEquals("ID", savedRegion)

    verify(mockDataStore).edit(any())
  }

  @Test
  fun saveRegion_updatesRegionWhenKeyExists() = runTest {
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
