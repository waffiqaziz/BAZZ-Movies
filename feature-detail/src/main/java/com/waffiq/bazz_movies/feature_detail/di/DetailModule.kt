package com.waffiq.bazz_movies.feature_detail.di

import com.waffiq.bazz_movies.feature_detail.domain.usecase.get_detail_movie.GetDetailMovieInteractor
import com.waffiq.bazz_movies.feature_detail.domain.usecase.get_detail_movie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.feature_detail.domain.usecase.get_detail_tv.GetDetailTvInteractor
import com.waffiq.bazz_movies.feature_detail.domain.usecase.get_detail_tv.GetDetailTvUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class DetailModule {

  @Binds
  @ViewModelScoped
  abstract fun provideDetailMovieUseCase(
    getDetailMovieInteractor: GetDetailMovieInteractor
  ): GetDetailMovieUseCase

  @Binds
  @ViewModelScoped
  abstract fun provideDetailTvUseCase(
    getDetailTvInteractor: GetDetailTvInteractor
  ): GetDetailTvUseCase
}