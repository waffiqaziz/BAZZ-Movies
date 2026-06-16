package com.waffiq.bazz_movies.core.user.di

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

  private const val OLD_UNENCRYPTED_FILE = "settings"
  private const val NEW_ENCRYPTED_FILE = "secure_settings"

  private const val KEYSET_NAME = "master_keyset"
  private const val PREFERENCE_FILE_NAME = "keyset_prefs"
  private const val MASTER_KEY_URI = "android-keystore://master_key"

  @Provides
  @Singleton
  fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
    // Initialize Tink configurations
    TinkConfig.register()

    // 1. Manage the cryptographic keyset using Android Keystore
    val keysetHandle = AndroidKeysetManager.Builder()
      .withSharedPref(appContext, KEYSET_NAME, PREFERENCE_FILE_NAME)
      .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
      .withMasterKeyUri(MASTER_KEY_URI)
      .build()
      .keysetHandle

    val aeadPrimitive = keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)

    // 2. Wrap PreferencesFileSerializer using Tink's authenticated encryption
    val secureSerializer = AeadSerializer(
      aead = aeadPrimitive,
      wrappedSerializer = PreferencesFileSerializer,
      associatedData = "${NEW_ENCRYPTED_FILE}.preferences_pb".encodeToByteArray()
    )

    // 3. Define the one-time Migration from the old unencrypted DataStore
    val migrationFromUnencrypted = object : DataMigration<Preferences> {
      override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        val oldFile = appContext.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE)
        // Migrate only if the old unencrypted file exists and the new encrypted store is blank
        return oldFile.exists() && currentData.asMap().isEmpty()
      }

      override suspend fun migrate(currentData: Preferences): Preferences {
        // Initialize the old unencrypted datastore temporarily to fetch legacy entries
        val legacyStore = PreferenceDataStoreFactory.create(
          produceFile = { appContext.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE) }
        )
        val legacyPreferences = legacyStore.data.first()

        // Merge legacy entries safely into our new encrypted instance
        val mutablePrefs = currentData.toMutablePreferences()
        legacyPreferences.asMap().forEach { (key, value) ->
          @Suppress("UNCHECKED_CAST")
          mutablePrefs[key as Preferences.Key<Any>] = value
        }
        return mutablePrefs
      }

      override suspend fun cleanUp() {
        // Wipe the legacy unencrypted file from disk safely
        try {
          appContext.preferencesDataStoreFile(OLD_UNENCRYPTED_FILE).delete()
        } catch (_: Exception) {
          // Suppress any deletion failures or lack of permissions
        }
      }
    }

    // 4. Construct the Encrypted DataStore instance
    val secureStorage = FileStorage(
      serializer = secureSerializer,
      produceFile = { appContext.preferencesDataStoreFile(NEW_ENCRYPTED_FILE) }
    )

    return DataStore.Builder(
      storage = secureStorage,
      context = SupervisorJob() + Dispatchers.IO,
    )
      .addMigrations(listOf(migrationFromUnencrypted))
      .build()
  }

  @Provides
  @Singleton
  fun provideUserPreference(dataStore: DataStore<Preferences>): UserPreference =
    UserPreference(dataStore)
}