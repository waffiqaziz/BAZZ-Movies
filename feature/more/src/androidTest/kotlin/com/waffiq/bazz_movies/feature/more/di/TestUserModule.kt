package com.waffiq.bazz_movies.feature.more.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.waffiq.bazz_movies.core.network.data.remote.datasource.UserDataSource
import com.waffiq.bazz_movies.core.user.data.model.UserPreference
import com.waffiq.bazz_movies.core.user.data.repository.UserRepository
import com.waffiq.bazz_movies.core.user.di.DatastoreModule
import com.waffiq.bazz_movies.core.user.di.UserRepositoryModule
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Dagger Hilt test module that provides test-specific implementations of
 * dependencies related to user preferences and repository.
 *
 * This module replaces [DatastoreModule] and [UserRepositoryModule] during instrumentation
 */
@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [DatastoreModule::class, UserRepositoryModule::class]
)
object TestUserModule {

  @Provides
  @Singleton
  fun provideTestDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
      produceFile = {
        context.preferencesDataStoreFile("test_user_data_${System.currentTimeMillis()}")
      }
    )
  }

  @Provides
  @Singleton
  fun provideUserPreference(dataStore: DataStore<Preferences>): UserPreference {
    return UserPreference(dataStore)
  }

  @Provides
  @Singleton
  fun provideUserRepository(
    userPreference: UserPreference,
    userDataSource: UserDataSource
  ): IUserRepository {
    return UserRepository(userPreference, userDataSource)
  }
}
