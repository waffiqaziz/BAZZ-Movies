package com.waffiq.bazz_movies.feature.favorite.di

import com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite.CheckAndAddToWatchlistInteractor
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.composite.CheckAndAddToWatchlistUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface CheckAndAddToWatchlistUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindCheckAndAddToWatchlistUseCaseModule(
    checkAndAddToWatchlistInteractor: CheckAndAddToWatchlistInteractor,
  ): CheckAndAddToWatchlistUseCase
}
