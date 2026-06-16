package com.waffiq.bazz_movies.core.user.di

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import com.waffiq.bazz_movies.core.user.utils.common.Constants.DATASTORE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

internal const val OLD_UNENCRYPTED_FILE = DATASTORE_NAME
internal const val NEW_ENCRYPTED_FILE = "secure_settings"

/**
 * Hilt module that provides the encrypted [DataStore] and [UserPreference].
 *
 * Data is encrypted with Tink (AES-256 GCM). The key is kept in the Android Keystore.
 * Data from the old unencrypted DataStore is moved to the new file on first run.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

  internal const val KEYSET_NAME = "master_keyset"
  internal const val PREFERENCE_FILE_NAME = "keyset_prefs"
  private const val MASTER_KEY_URI = "android-keystore://master_key"

  @Provides
  @Singleton
  fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
    buildDataStore(appContext, ::createKeysetHandle, SupervisorJob() + Dispatchers.IO)

  internal fun buildDataStore(
    appContext: Context,
    keysetFactory: (Context) -> KeysetHandle,
    coroutineContext: CoroutineContext,
  ): DataStore<Preferences> {
    TinkConfig.register()

    // create Aead to encrypt/decrypt data
    val aead = buildKeysetHandle(appContext, keysetFactory)
      .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    return createDataStore(appContext, aead, coroutineContext)
  }

  internal fun buildKeysetHandle(
    context: Context,
    keysetFactory: (Context) -> KeysetHandle,
  ): KeysetHandle =
    try {
      keysetFactory(context)
    } catch (_: GeneralSecurityException) {
      reset(context);
      keysetFactory(context)
    } catch (_: IOException) {
      reset(context);
      keysetFactory(context)
    }

  // deletes saved keyset and the encrypted DataStore file
  private fun reset(context: Context) {
    context.deleteSharedPreferences(PREFERENCE_FILE_NAME)
    context.preferencesDataStoreFile(NEW_ENCRYPTED_FILE).delete()
  }

  // creates the [KeysetHandle] using AES-256 GCM.
  internal fun createKeysetHandle(context: Context): KeysetHandle =
    AndroidKeysetManager.Builder()
      .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE_NAME)
      .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
      .withMasterKeyUri(MASTER_KEY_URI)
      .build()
      .keysetHandle

  // Creates the [DataStore] that encrypts data with [aead]
  // - Existing data in the old unencrypted file is migrated, then the old file is deleted
  // - If the file is corrupt, then replace with empty preferences
  internal fun createDataStore(
    appContext: Context,
    aead: Aead,
    coroutineContext: CoroutineContext,
  ): DataStore<Preferences> {
    // wrap PreferencesFileSerializer using Tink authenticated encryption
    val secureSerializer = AeadSerializer(
      aead = aead,
      wrappedSerializer = PreferencesFileSerializer,
      associatedData = "${NEW_ENCRYPTED_FILE}.preferences_pb".encodeToByteArray(),
    )

    // migration from old unencrypted DataStore
    val migrationFromUnencrypted = object : DataMigration<Preferences> {

      // migrates only if the old unencrypted file still exists
      override suspend fun shouldMigrate(currentData: Preferences) =
        appContext.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE).exists()

      override suspend fun migrate(currentData: Preferences): Preferences {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        try {
          val legacy = PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = { appContext.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE) },
          ).data.first()
          val merged = legacy.toMutablePreferences()
          merged += currentData
          return merged
        } finally {
          scope.cancel()
        }
      }

      // deletes old unencrypted file after migration
      override suspend fun cleanUp() {
        appContext.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE).delete()
      }
    }

    // construct the Encrypted DataStore instance
    val secureStorage = FileStorage(
      serializer = secureSerializer,
      produceFile = { appContext.preferencesDataStoreFile(NEW_ENCRYPTED_FILE) },
    )

    return DataStore.Builder(
      storage = secureStorage,
      context = coroutineContext,
    )
      // prevent crashing when exception is occurs
      .setCorruptionHandler(ReplaceFileCorruptionHandler { emptyPreferences() })
      .addMigrations(listOf(migrationFromUnencrypted))
      .build()
  }

  @Provides
  @Singleton
  fun provideUserPreference(dataStore: DataStore<Preferences>): UserPreference =
    UserPreference(dataStore)
}
