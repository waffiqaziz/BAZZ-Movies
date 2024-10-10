package com.waffiq.bazz_movies.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.waffiq.bazz_movies.data.local.model.UserPreference
import com.waffiq.bazz_movies.utils.common.Constants.DATASTORE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

  @Provides
  @Singleton
  fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
      produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME) }
    )
  }

  @Provides
  @Singleton
  fun provideUserPreference(dataStore: DataStore<Preferences>): UserPreference {
    return UserPreference(dataStore)
  }
}