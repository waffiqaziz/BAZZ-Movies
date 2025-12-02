package com.waffiq.bazz_movies.feature.home.di

import com.waffiq.bazz_movies.feature.home.data.repository.HomeRepositoryImpl
import com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface HomeRepositoryModule {

  @Binds
  fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): IHomeRepository
}
