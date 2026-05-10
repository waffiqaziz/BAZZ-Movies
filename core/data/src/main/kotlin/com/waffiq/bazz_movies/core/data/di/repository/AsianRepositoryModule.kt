package com.waffiq.bazz_movies.core.data.di.repository

import com.waffiq.bazz_movies.core.data.data.repository.AsianRepositoryImpl
import com.waffiq.bazz_movies.core.data.domain.repository.IAsianRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
interface AsianRepositoryModule {

  @Binds
  fun bindAsianRepository(asianRepositoryImpl: AsianRepositoryImpl): IAsianRepository
}
