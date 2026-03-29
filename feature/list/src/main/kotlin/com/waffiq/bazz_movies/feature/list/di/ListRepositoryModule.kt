package com.waffiq.bazz_movies.feature.list.di

import com.waffiq.bazz_movies.feature.list.data.repository.ListRepositoryImpl
import com.waffiq.bazz_movies.feature.list.domain.repository.IListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface ListRepositoryModule {

  @Binds
  fun bindListRepository(listRepositoryImpl: ListRepositoryImpl): IListRepository
}
