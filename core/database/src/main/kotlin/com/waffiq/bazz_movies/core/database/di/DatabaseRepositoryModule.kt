package com.waffiq.bazz_movies.core.database.di

import com.waffiq.bazz_movies.core.database.data.repository.DatabaseRepositoryImpl
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface DatabaseRepositoryModule {

  @Binds
  fun bindDatabaseRepository(
    databaseRepository: DatabaseRepositoryImpl,
  ): IDatabaseRepository
}
