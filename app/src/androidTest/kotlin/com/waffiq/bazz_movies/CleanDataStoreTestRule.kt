package com.waffiq.bazz_movies

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File
import java.util.UUID

class CleanDataStoreTestRule : TestWatcher() {

  override fun starting(description: Description) {
    replaceDataStoreNamesWithRandomUuids()
    super.starting(description)
  }

  override fun finished(description: Description) {
    super.finished(description)
    removeDataStoreFiles()
  }

  private fun replaceDataStoreNamesWithRandomUuids() {
    mockkStatic("androidx.datastore.DataStoreFile")
    val contextSlot = slot<Context>()
    every {
      capture(contextSlot).dataStoreFile(any())
    } answers {
      File(
        contextSlot.captured.filesDir,
        "datastore/${UUID.randomUUID()}.preferences_pb",
      )
    }
  }

  private fun removeDataStoreFiles() {
    InstrumentationRegistry.getInstrumentation().targetContext.run {
      File(filesDir, "datastore").deleteRecursively()
    }
  }
}