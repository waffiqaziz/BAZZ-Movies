package com.waffiq.bazz_movies.core.data.di.repository

import com.waffiq.bazz_movies.core.data.data.repository.TvRepositoryImpl
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface TvRepositoryModule {

  @Binds
  fun bindTvRepository(tvRepositoryImpl: TvRepositoryImpl): ITvRepository
}
