package com.waffiq.bazz_movies.core.movie.di

import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetMovieStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateInteractor
import com.waffiq.bazz_movies.core.movie.domain.usecase.mediastate.GetTvStateUseCase
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
    getStatedMovieInteractor: GetMovieStateInteractor
  ): GetMovieStateUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindStatedTvUseCase(
    getStatedTvInteractor: GetTvStateInteractor
  ): GetTvStateUseCase
}
