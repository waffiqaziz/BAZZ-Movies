package com.waffiq.bazz_movies.feature.watchlist.di

import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlistmovie.GetWatchlistMovieInteractor
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlistmovie.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlisttv.GetWatchlistTvInteractor
import com.waffiq.bazz_movies.feature.watchlist.domain.usecase.watchlisttv.GetWatchlistTvUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface WatchlistUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindWatchlistMovieUseCase(
    getWatchlistMovieInteractor: GetWatchlistMovieInteractor,
  ): GetWatchlistMovieUseCase

  @Binds
  @ViewModelScoped
  fun bindWatchlistTvUseCase(
    getWatchlistTvInteractor: GetWatchlistTvInteractor,
  ): GetWatchlistTvUseCase
}
