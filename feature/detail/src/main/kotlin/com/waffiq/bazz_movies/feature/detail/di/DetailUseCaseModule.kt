package com.waffiq.bazz_movies.feature.detail.di

import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface DetailUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindDetailMovieUseCase(
    getDetailMovieInteractor: GetMovieDetailInteractor,
  ): GetMovieDetailUseCase

  @Binds
  @ViewModelScoped
  fun bindDetailTvUseCase(getDetailTvInteractor: GetTvDetailInteractor): GetTvDetailUseCase

  @Binds
  @ViewModelScoped
  fun bindDetailOMDbUseCase(getDetailOMDbInteractor: GetOMDbDetailInteractor): GetOMDbDetailUseCase
}
