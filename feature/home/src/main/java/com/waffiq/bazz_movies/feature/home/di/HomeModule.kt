package com.waffiq.bazz_movies.feature.home.di

import com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesInteractor
import com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvInteractor
import com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeModule {

  @Binds
  @ViewModelScoped
  abstract fun bindListMoviesUseCase(
    getListMoviesInteractor: GetListMoviesInteractor
  ): GetListMoviesUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindListTvUseCase(
    getListTvInteractor: GetListTvInteractor
  ): GetListTvUseCase
}
