package com.waffiq.bazz_movies.core.di

import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedMovieInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedTvInteractor
import com.waffiq.bazz_movies.core.domain.usecase.get_stated.GetStatedTvUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class StatedModule {

  @Binds
  @ViewModelScoped
  abstract fun bindStatedMovieUseCase(
    getStatedMovieInteractor: GetStatedMovieInteractor
  ): GetStatedMovieUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindStatedTvUseCase(
    getStatedTvInteractor: GetStatedTvInteractor
  ): GetStatedTvUseCase
}
