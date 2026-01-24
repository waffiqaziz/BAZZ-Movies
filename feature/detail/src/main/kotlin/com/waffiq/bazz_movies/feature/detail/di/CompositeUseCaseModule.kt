package com.waffiq.bazz_movies.feature.detail.di

import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaStateWithUserInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaStateWithUserUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMovieDataWithUserRegionInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMovieDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvAllScoreInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvAllScoreUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvDataWithUserRegionInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostRateInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostRateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface CompositeUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindMovieDetailWithUserPrefUseCaseSingleton(
    getMovieDetailWithUserPrefInteractor: GetMovieDataWithUserRegionInteractor,
  ): GetMovieDataWithUserRegionUseCase

  @Binds
  @ViewModelScoped
  fun bindTvDetailWithUserPrefUseCaseSingleton(
    getTvDetailWithUserPrefInteractor: GetTvDataWithUserRegionInteractor,
  ): GetTvDataWithUserRegionUseCase

  @Binds
  @ViewModelScoped
  fun bindMediaStateWithUserUseCaseSingleton(
    getMediaStateWithUserInteractor: GetMediaStateWithUserInteractor,
  ): GetMediaStateWithUserUseCase

  @Binds
  @ViewModelScoped
  fun bindPostMethodWithUserUseCaseSingleton(
    postMethodWithUserInteractor: PostRateInteractor,
  ): PostRateUseCase

  @Binds
  @ViewModelScoped
  fun bindGetTvAllScoreUseCaseSingleton(
    getTvAllScoreInteractor: GetTvAllScoreInteractor,
  ): GetTvAllScoreUseCase
}
