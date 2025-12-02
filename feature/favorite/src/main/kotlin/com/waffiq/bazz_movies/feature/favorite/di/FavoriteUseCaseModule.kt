package com.waffiq.bazz_movies.feature.favorite.di

import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteMovieInteractor
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteTvInteractor
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteTvUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface FavoriteUseCaseModule {

  // region MOVIE
  @Binds
  @ViewModelScoped
  fun bindFavoriteMovieUseCase(
    getFavoriteMovieInteractor: GetFavoriteMovieInteractor,
  ): GetFavoriteMovieUseCase

  // region TV
  @Binds
  @ViewModelScoped
  fun bindFavoriteTvUseCase(
    getFavoriteTvInteractor: GetFavoriteTvInteractor,
  ): GetFavoriteTvUseCase
}
