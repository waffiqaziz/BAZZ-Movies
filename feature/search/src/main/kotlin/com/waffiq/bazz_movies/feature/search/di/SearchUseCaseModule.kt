package com.waffiq.bazz_movies.feature.search.di

import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchInteractor
import com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchUseCaseModule {

  @Binds
  @ViewModelScoped
  abstract fun bindMultiSearchUseCase(
    multiSearchInteractor: MultiSearchInteractor
  ): MultiSearchUseCase
}
