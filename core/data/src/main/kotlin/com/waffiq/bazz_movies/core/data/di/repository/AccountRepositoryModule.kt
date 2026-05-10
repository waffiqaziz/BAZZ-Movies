package com.waffiq.bazz_movies.core.data.di.repository

import com.waffiq.bazz_movies.core.data.data.repository.AccountRepositoryImpl
import com.waffiq.bazz_movies.core.data.domain.repository.IAccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
interface AccountRepositoryModule {

  @Binds
  fun bindAccountRepository(discoverRepositoryImpl: AccountRepositoryImpl): IAccountRepository
}
