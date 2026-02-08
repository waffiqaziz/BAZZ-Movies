package com.waffiq.bazz_movies.feature.favorite.di

import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie.GetFavoriteMovieInteractor
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritemovie.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv.GetFavoriteTvInteractor
import com.waffiq.bazz_movies.feature.favorite.domain.usecase.favoritetv.GetFavoriteTvUseCase
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
  fun bindFavoriteTvUseCase(getFavoriteTvInteractor: GetFavoriteTvInteractor): GetFavoriteTvUseCase
}
