package com.waffiq.bazz_movies.core.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import java.security.GeneralSecurityException

@RunWith(RobolectricTestRunner::class)
class DatastoreModuleTest {

  private val key = stringPreferencesKey("token")
  private val jobs = mutableListOf<Job>()

  private lateinit var context: Context
  private lateinit var aead: Aead

  @Before
  fun setUp() {
    AeadConfig.register()
    aead = newAead()
    context = ApplicationProvider.getApplicationContext()
  }

  @After
  fun tearDown() = runBlocking { closeStores() }

  private fun newAead(): Aead =
    KeysetHandle.generateNew(PredefinedAeadParameters.AES256_GCM)
      .getPrimitive(RegistryConfiguration.get(), Aead::class.java)

  private fun newStore(withAead: Aead = aead): DataStore<Preferences> {
    val job = Job().also { jobs += it }
    return DatastoreModule.createDataStore(context, withAead, job + Dispatchers.IO)
  }

  // close before reopening to prevent DataStore throws if two instances are active on one file
  private suspend fun closeStores() {
    jobs.forEach { it.cancelAndJoin() }
    jobs.clear()
  }

  private suspend fun writeLegacyPlaintext(value: String) {
    val job = Job()
    PreferenceDataStoreFactory.create(
      scope = CoroutineScope(job + Dispatchers.IO),
      produceFile = { context.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE) },
    ).edit { it[key] = value }
    job.cancelAndJoin()
  }

  private fun freshKeyset(): KeysetHandle =
    KeysetHandle.generateNew(PredefinedAeadParameters.AES256_GCM)

  private fun seedStaleState() {
    context.getSharedPreferences(DatastoreModule.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
      .edit().putString("stale", "x").commit()
    encryptedFile().apply {
        parentFile?.mkdirs();
        writeText("stale")
    }
  }

  private fun assertStaleStateWiped() {
    val prefs = context.getSharedPreferences(
      DatastoreModule.PREFERENCE_FILE_NAME,
      Context.MODE_PRIVATE,
    )
    assertTrue(prefs.all.isEmpty())
    assertFalse(encryptedFile().exists())
  }

  private fun legacyFile() = context.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE)
  private fun encryptedFile() = context.preferencesDataStoreFile(NEW_ENCRYPTED_FILE)

  @Test
  fun provideDataStore_buildsKeysetThroughAndroidKeysetManager() =
    runTest {
      mockkConstructor(AndroidKeysetManager.Builder::class)
      val manager = mockk<AndroidKeysetManager> {
        every { keysetHandle } returns freshKeyset()
      }
      every { anyConstructed<AndroidKeysetManager.Builder>().build() } returns manager

      val store = DatastoreModule.provideDataStore(context)
      store.edit { it[key] = "real-path" }

      assertEquals("real-path", store.data.first()[key])
      verify(exactly = 1) { anyConstructed<AndroidKeysetManager.Builder>().build() }
    }

  @Test
  fun datastore_whenWriteThenRead_returnsSavedValue() =
    runTest {
      val store = newStore()
      store.edit { it[key] = "hello" }

      assertEquals("hello", store.data.first()[key])
    }

  @Test
  fun dataOnDisk_isNotPlaintext() =
    runTest {
      newStore().edit { it[key] = "super-secret-value" }
      closeStores()

      val raw = String(encryptedFile().readBytes(), Charsets.ISO_8859_1)
      assertFalse(raw.contains("super-secret-value"))
    }

  @Test
  fun dataSurvives_whenReopen_returnsSameKey() =
    runTest {
      newStore().edit { it[key] = "persisted" }
      closeStores()

      assertEquals("persisted", newStore().data.first()[key])
    }

  @Test
  fun migration_copiesLegacyValues_andDeletesPlaintextFile() =
    runTest {
      writeLegacyPlaintext("legacy-value")
      assertTrue(legacyFile().exists())

      val prefs = newStore().data.first()

      assertEquals("legacy-value", prefs[key])
      assertFalse(legacyFile().exists())
      assertTrue(encryptedFile().exists())
    }

  @Test
  fun migration_whenKeyExistsInBothStores_keepsEncryptedValue() =
    runTest {
      newStore().edit { it[key] = "encrypted-wins" }
      closeStores()
      writeLegacyPlaintext("legacy-loses")

      val prefs = newStore().data.first()

      assertEquals("encrypted-wins", prefs[key])
      assertFalse(legacyFile().exists())
    }

  @Test
  fun datastore_whenNoLegacyFile_migrationIsSkipped() =
    runTest {
      val prefs = newStore().data.first()

      assertTrue(prefs.asMap().isEmpty())
      assertFalse(legacyFile().exists())
    }

  @Test
  fun wrongKey_triggersCorruptionHandler_andStoreRecovers() =
    runTest {
      newStore().edit { it[key] = "old-data" }
      closeStores()

      val store = newStore(withAead = newAead()) // different key, can't decrypt

      assertTrue(store.data.first().asMap().isEmpty()) // no crash, reset to empty
      store.edit { it[key] = "fresh" }
      assertEquals("fresh", store.data.first()[key])
    }

  @Test
  fun provideUserPreference_whenCalled_returnsInstance() =
    runTest {
      val userPreference = DatastoreModule.provideUserPreference(newStore())

      assertNotNull(userPreference)
    }

  @Test
  fun buildKeysetHandle_success_doesNotReset() {
    seedStaleState()

    DatastoreModule.buildKeysetHandle(context) { freshKeyset() }

    assertTrue(encryptedFile().exists()) // untouched
  }

  @Test
  fun buildKeysetHandle_generalSecurityException_resetsAndRetries() {
    seedStaleState()
    var calls = 0

    val handle = DatastoreModule.buildKeysetHandle(context) {
      if (calls++ == 0) throw GeneralSecurityException("empty keyset") else freshKeyset()
    }

    assertNotNull(handle)
    assertEquals(2, calls)
    assertStaleStateWiped()
  }

  @Test
  fun buildKeysetHandle_ioException_resetsAndRetries() {
    seedStaleState()
    var calls = 0

    val handle = DatastoreModule.buildKeysetHandle(context) {
      if (calls++ == 0) throw IOException("read failed") else freshKeyset()
    }

    assertNotNull(handle)
    assertEquals(2, calls)
    assertStaleStateWiped()
  }

  @Test
  fun buildKeysetHandle_retryAlsoFails_propagates() {
    assertThrows(GeneralSecurityException::class.java) {
      DatastoreModule.buildKeysetHandle(context) { throw GeneralSecurityException("broken") }
    }
  }

  @Test
  fun buildDataStore_usesFactory_andReadsAndWrites() =
    runTest {
      val job = Job().also { jobs += it }
      val store = DatastoreModule.buildDataStore(context, { freshKeyset() }, job + Dispatchers.IO)

      store.edit { it[key] = "via-build" }

      assertEquals("via-build", store.data.first()[key])
    }

  @Test
  fun buildDataStore_whenFirstKeysetAttemptFails_recovers() =
    runTest {
      seedStaleState()
      var calls = 0
      val job = Job().also { jobs += it }

      val store = DatastoreModule.buildDataStore(
        context,
        { if (calls++ == 0) throw GeneralSecurityException("boom") else freshKeyset() },
        job + Dispatchers.IO,
      )

      assertTrue(store.data.first().asMap().isEmpty())
    }
}
