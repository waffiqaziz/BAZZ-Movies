package com.waffiq.bazz_movies.feature.watchlist.di

import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.composite.CheckAndAddToFavoriteInteractor
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.composite.CheckAndAddToFavoriteUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface CheckAndAddToFavoriteUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindCheckAndAddToFavoriteUseCaseModule(
    checkAndAddToFavoriteInteractor: CheckAndAddToFavoriteInteractor,
  ): CheckAndAddToFavoriteUseCase
}
