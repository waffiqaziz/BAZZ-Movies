package com.waffiq.bazz_movies.feature_search.di

import com.waffiq.bazz_movies.feature_search.domain.usecase.MultiSearchInteractor
import com.waffiq.bazz_movies.feature_search.domain.usecase.MultiSearchUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchModule {

  @Binds
  @ViewModelScoped
  abstract fun provideMultiSearchUseCase(
    multiSearchInteractor: MultiSearchInteractor
  ): MultiSearchUseCase
}