package com.waffiq.bazz_movies.core.di

import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteMovieInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteTvInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistMovieInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistTvInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.local_database.LocalDatabaseInteractor
import com.waffiq.bazz_movies.core.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.domain.usecase.post_method.PostMethodInteractor
import com.waffiq.bazz_movies.core.domain.usecase.post_method.PostMethodUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class CoreModule {

  // region MOVIE
  @Binds
  @ViewModelScoped
  abstract fun bindFavoriteMovieUseCase(
    getFavoriteMovieInteractor: GetFavoriteMovieInteractor
  ): GetFavoriteMovieUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindWatchlistMovieUseCase(
    getWatchlistMovieInteractor: GetWatchlistMovieInteractor
  ): GetWatchlistMovieUseCase
  // endregion MOVIE

  // region TV
  @Binds
  @ViewModelScoped
  abstract fun bindFavoriteTvUseCase(
    getFavoriteTvInteractor: GetFavoriteTvInteractor
  ): GetFavoriteTvUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindWatchlistTvUseCase(
    getWatchlistTvInteractor: GetWatchlistTvInteractor
  ): GetWatchlistTvUseCase
  // endregion TV

  @Binds
  @ViewModelScoped
  abstract fun bindPostMethodUseCase(
    postMethodInteractor: PostMethodInteractor
  ): PostMethodUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindLocalDatabaseUseCase(
    localDatabaseInteractor: LocalDatabaseInteractor
  ): LocalDatabaseUseCase
}
