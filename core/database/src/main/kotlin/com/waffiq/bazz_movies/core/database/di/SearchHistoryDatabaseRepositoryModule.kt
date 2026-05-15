package com.waffiq.bazz_movies.core.database.di

import com.waffiq.bazz_movies.core.database.data.repository.SearchHistoryLocalDatabaseRepositoryImpl
import com.waffiq.bazz_movies.core.database.domain.repository.ISearchHistoryLocalDatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface SearchHistoryDatabaseRepositoryModule {

  @Binds
  fun bindDatabaseRepository(
    databaseRepository: SearchHistoryLocalDatabaseRepositoryImpl,
  ): ISearchHistoryLocalDatabaseRepository
}
