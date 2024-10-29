package com.waffiq.bazz_movies.feature.detail.di

import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb.GetDetailOMDbInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb.GetDetailOMDbUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie.GetDetailMovieInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv.GetDetailTvInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv.GetDetailTvUseCase
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

  @Binds
  @ViewModelScoped
  abstract fun provideDetailOMDbUseCase(
    getDetailOMDbInteractor: GetDetailOMDbInteractor
  ): GetDetailOMDbUseCase
}