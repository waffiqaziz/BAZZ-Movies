package com.waffiq.bazz_movies.feature.detail.di

import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaDetailInteractor
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaDetailUseCase
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
  fun bindMediaDetailUseCaseSingleton(
    getMediaDetailInteractor: GetMediaDetailInteractor,
  ): GetMediaDetailUseCase

  @Binds
  @ViewModelScoped
  fun bindPostMethodWithUserUseCaseSingleton(
    postMethodWithUserInteractor: PostRateInteractor,
  ): PostRateUseCase
}
