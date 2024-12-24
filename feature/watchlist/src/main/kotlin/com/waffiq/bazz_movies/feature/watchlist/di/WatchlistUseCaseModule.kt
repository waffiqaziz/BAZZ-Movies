package com.waffiq.bazz_movies.feature.watchlist.di

import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistMovieInteractor
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistTvInteractor
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistTvUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class WatchlistUseCaseModule {

  @Binds
  @ViewModelScoped
  abstract fun bindWatchlistMovieUseCase(
    getWatchlistMovieInteractor: GetWatchlistMovieInteractor
  ): GetWatchlistMovieUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindWatchlistTvUseCase(
    getWatchlistTvInteractor: GetWatchlistTvInteractor
  ): GetWatchlistTvUseCase
}
