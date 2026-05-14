package com.waffiq.bazz_movies.core.database.di

import com.waffiq.bazz_movies.core.database.data.repository.FavoriteLocalDatabaseRepositoryImpl
import com.waffiq.bazz_movies.core.database.domain.repository.IFavoriteLocalDatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface FavoriteDatabaseRepositoryModule {

  @Binds
  fun bindDatabaseRepository(
    databaseRepository: FavoriteLocalDatabaseRepositoryImpl,
  ): IFavoriteLocalDatabaseRepository
}
