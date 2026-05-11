package com.waffiq.bazz_movies.core.data.di.repository

import com.waffiq.bazz_movies.core.data.data.repository.TrendingRepositoryImpl
import com.waffiq.bazz_movies.core.data.domain.repository.ITrendingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface TrendingRepositoryModule {

  @Binds
  fun bindTrendingRepository(trendingRepositoryImpl: TrendingRepositoryImpl): ITrendingRepository
}
