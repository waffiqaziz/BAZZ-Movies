package com.waffiq.bazz_movies.feature.detail.di

import com.waffiq.bazz_movies.feature.detail.data.repository.DetailRepositoryImpl
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface DetailRepositoryModule {

  @Binds
  fun bindDetailRepository(
    detailRepositoryImpl: DetailRepositoryImpl,
  ): IDetailRepository
}
