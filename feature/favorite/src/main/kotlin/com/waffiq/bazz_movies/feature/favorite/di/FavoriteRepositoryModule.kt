package com.waffiq.bazz_movies.feature.favorite.di

import com.waffiq.bazz_movies.feature.favorite.data.repository.FavoriteRepositoryImpl
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface FavoriteRepositoryModule {

  @Binds
  fun bindFavoriteRepository(favoriteRepository: FavoriteRepositoryImpl): IFavoriteRepository
}
