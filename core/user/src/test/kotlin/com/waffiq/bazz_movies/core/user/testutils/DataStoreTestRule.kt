package com.waffiq.bazz_movies.core.user.testutils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File

class DataStoreTestRule : TestWatcher() {
  lateinit var testDataStore: DataStore<Preferences>
    private set

  override fun starting(description: Description) {
    val testFile = File.createTempFile("test_${description.methodName}_${System.nanoTime()}", ".preferences_pb")
    testDataStore = PreferenceDataStoreFactory.create { testFile }
  }
}
