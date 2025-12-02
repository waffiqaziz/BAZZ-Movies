package com.waffiq.bazz_movies.feature.person.di

import com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonInteractor
import com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
interface PersonUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindDetailPersonUseCase(
    getDetailPersonInteractor: GetDetailPersonInteractor,
  ): GetDetailPersonUseCase
}
