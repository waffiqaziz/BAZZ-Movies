package com.waffiq.bazz_movies.core.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.waffiq.bazz_movies.core.user.utils.common.Constants.DATASTORE_NAME
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class DatastoreModuleTest {

  private val mockContext: Context = mockk(relaxed = true)
  private val datastoreModule : DatastoreModule = DatastoreModule()

  @Test
  fun provideDataStore_BothReturnsDataStoreInstance() = runTest {
    val actualDatastore = datastoreModule.provideDataStore(mockContext)
    val expectedDatastore = PreferenceDataStoreFactory.create(produceFile = {
      mockContext.preferencesDataStoreFile(DATASTORE_NAME)
    })

    assertNotNull(actualDatastore.data)
    assertNotNull(expectedDatastore.data)
  }


  @Test
  fun provideUserPreference_ReturnsUserPreferenceInstance() = runTest {
    val dataStore: DataStore<Preferences> = datastoreModule.provideDataStore(mockContext)
    val userPreference = datastoreModule.provideUserPreference(dataStore)

    assertNotNull(userPreference)
  }
}
