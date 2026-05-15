package com.waffiq.bazz_movies.core.database.domain.di

import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseInteractor
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface FavoriteLocalDatabaseUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindFavoriteLocalDatabaseUseCase(
    favoriteLocalDatabaseInteractor: FavoriteLocalDatabaseInteractor,
  ): FavoriteLocalDatabaseUseCase
}
