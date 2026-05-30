package com.waffiq.bazz_movies.core.database.di

import android.content.Context
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.database.data.manager.DatabaseBackupManager
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
class DatabaseBackupManagerModule {

  @Provides
  @Singleton
  fun provideDatabaseBackupManager(
    @ApplicationContext context: Context,
    favoriteDao: FavoriteDao,
    @AppVersion appVersion: String,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
  ): DatabaseBackupManager = DatabaseBackupManager(context, favoriteDao, ioDispatcher, appVersion)
}
