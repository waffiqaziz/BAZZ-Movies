package com.waffiq.bazz_movies.core.database.domain.di

import com.waffiq.bazz_movies.core.database.domain.usecase.DatabaseBackupInteractor
import com.waffiq.bazz_movies.core.database.domain.usecase.DatabaseBackupUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface DatabaseBackupUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindDatabaseBackupUseCase(
    databaseBackupUseCaseInteractor: DatabaseBackupInteractor,
  ): DatabaseBackupUseCase
}
