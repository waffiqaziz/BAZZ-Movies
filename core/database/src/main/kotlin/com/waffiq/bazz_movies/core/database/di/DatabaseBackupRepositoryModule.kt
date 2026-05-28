package com.waffiq.bazz_movies.core.database.di

import com.waffiq.bazz_movies.core.database.data.repository.DatabaseBackupRepositoryImpl
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseBackupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface DatabaseBackupRepositoryModule {

  @Binds
  fun bindDatabaseBackupRepository(
    databaseBackupRepository: DatabaseBackupRepositoryImpl,
  ): IDatabaseBackupRepository
}
