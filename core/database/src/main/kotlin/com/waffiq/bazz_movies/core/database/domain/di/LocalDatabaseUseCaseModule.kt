package com.waffiq.bazz_movies.core.database.domain.di

import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseInteractor
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalDatabaseUseCaseModule {

  @Binds
  @ViewModelScoped
  abstract fun bindLocalDatabaseUseCase(
    localDatabaseInteractor: LocalDatabaseInteractor
  ): LocalDatabaseUseCase
}
