package com.waffiq.bazz_movies.core.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestDatastoreModule {

  @Provides
  @Singleton
  fun provideTestDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
      produceFile = { File(context.cacheDir, "test_datastore.preferences_pb") }
    )
  }

  @Provides
  @Singleton
  fun provideTestUserPreference(dataStore: DataStore<Preferences>): UserPreference {
    return UserPreference(dataStore)
  }
}
