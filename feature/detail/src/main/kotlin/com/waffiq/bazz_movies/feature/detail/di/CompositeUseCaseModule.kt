package com.waffiq.bazz_movies.feature.detail.di

import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaStateWithUserInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaStateWithUserUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMovieDataWithUserRegionInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMovieDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvDataWithUserRegionInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostMethodWithUserInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostMethodWithUserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class CompositeUseCaseModule {

  @Binds
  @ViewModelScoped
  abstract fun bindMovieDetailWithUserPrefUseCaseSingleton(
    getMovieDetailWithUserPrefInteractor: GetMovieDataWithUserRegionInteractor,
  ): GetMovieDataWithUserRegionUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindTvDetailWithUserPrefUseCaseSingleton(
    getTvDetailWithUserPrefInteractor: GetTvDataWithUserRegionInteractor,
  ): GetTvDataWithUserRegionUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindMediaStateWithUserUseCaseSingleton(
    getMediaStateWithUserInteractor: GetMediaStateWithUserInteractor,
  ): GetMediaStateWithUserUseCase

  @Binds
  @ViewModelScoped
  abstract fun bindPostMethodWithUserUseCaseSingleton(
    postMethodWithUserInteractor: PostMethodWithUserInteractor,
  ): PostMethodWithUserUseCase
}
