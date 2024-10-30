package com.waffiq.bazz_movies.di

import com.waffiq.bazz_movies.core.domain.usecase.auth_tmdb_account.AuthTMDbAccountInteractor
import com.waffiq.bazz_movies.core.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteMovieInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteTvInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_favorite.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_region.GetRegionInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_region.GetRegionUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistMovieInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistTvInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_watchlist.GetWatchlistTvUseCase
import com.waffiq.bazz_movies.core.domain.usecase.local_database.LocalDatabaseInteractor
import com.waffiq.bazz_movies.core.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.domain.usecase.post_method.PostMethodInteractor
import com.waffiq.bazz_movies.core.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.core.domain.usecase.user_pref.UserPrefInteractor
import com.waffiq.bazz_movies.core.domain.usecase.user_pref.UserPrefUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

  // region USER
  @Binds
  @ViewModelScoped
  abstract fun provideAuthTMDbAccountUseCase(
    authTMDbAccountInteractor: AuthTMDbAccountInteractor
  ): AuthTMDbAccountUseCase

  @Binds
  @ViewModelScoped
  abstract fun provideUserPrefUseCase(
    userPrefInteractor: UserPrefInteractor
  ): UserPrefUseCase

  @Binds
  @ViewModelScoped
  abstract fun provideGetRegionUseCase(
    getRegionInteractor: GetRegionInteractor
  ): GetRegionUseCase
  // endregion USER

  // region MOVIE
  @Binds
  @ViewModelScoped
  abstract fun provideFavoriteMovieUseCase(
    getFavoriteMovieInteractor: GetFavoriteMovieInteractor
  ): GetFavoriteMovieUseCase

  @Binds
  @ViewModelScoped
  abstract fun provideWatchlistMovieUseCase(
    getWatchlistMovieInteractor: GetWatchlistMovieInteractor
  ): GetWatchlistMovieUseCase
  // endregion MOVIE

  // region TV
  @Binds
  @ViewModelScoped
  abstract fun provideFavoriteTvUseCase(
    getFavoriteTvInteractor: GetFavoriteTvInteractor
  ): GetFavoriteTvUseCase

  @Binds
  @ViewModelScoped
  abstract fun provideWatchlistTvUseCase(
    getWatchlistTvInteractor: GetWatchlistTvInteractor
  ): GetWatchlistTvUseCase
  // endregion TV

  @Binds
  @ViewModelScoped
  abstract fun providePostMethodUseCase(
    postMethodInteractor: PostMethodInteractor
  ): PostMethodUseCase

  @Binds
  @ViewModelScoped
  abstract fun provideLocalDatabaseUseCase(
    localDatabaseInteractor: LocalDatabaseInteractor
  ): LocalDatabaseUseCase
}