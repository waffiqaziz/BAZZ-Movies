package com.waffiq.bazz_movies.feature.search.di

import com.waffiq.bazz_movies.feature.search.data.repository.SearchRepositoryImpl
import com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {

  @Binds
  abstract fun bindDetailRepository(
    searchRepositoryImpl: SearchRepositoryImpl
  ): ISearchRepository
}