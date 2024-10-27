package com.waffiq.bazz_movies.feature_person.di

import com.waffiq.bazz_movies.feature_person.domain.usecase.GetDetailPersonInteractor
import com.waffiq.bazz_movies.feature_person.domain.usecase.GetDetailPersonUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class PersonModule {

  @Binds
  @ViewModelScoped
  abstract fun provideDetailPersonUseCase(
    getDetailPersonInteractor: GetDetailPersonInteractor
  ): GetDetailPersonUseCase
}
