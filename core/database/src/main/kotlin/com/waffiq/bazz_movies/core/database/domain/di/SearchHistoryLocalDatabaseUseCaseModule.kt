package com.waffiq.bazz_movies.core.database.domain.di

import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseInteractor
import com.waffiq.bazz_movies.core.database.domain.usecase.SearchHistoryLocalDatabaseUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
fun interface SearchHistoryLocalDatabaseUseCaseModule {

  @Binds
  @ViewModelScoped
  fun bindSearchHistoryLocalDatabaseUseCase(
    searchHistoryLocalDatabaseInteractor: SearchHistoryLocalDatabaseInteractor,
  ): SearchHistoryLocalDatabaseUseCase
}
