package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedMovieInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedTvInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetTvStateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class StatedUseCaseModule {

  @Binds
  @ViewModelScoped
  abstract fun bindStatedMovieUseCase(
    getStatedMovieInteractor: GetStatedMovieInteractor
  ): GetMovieStateUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindStatedTvUseCase(
    getStatedTvInteractor: GetStatedTvInteractor
  ): GetTvStateUseCase
}
