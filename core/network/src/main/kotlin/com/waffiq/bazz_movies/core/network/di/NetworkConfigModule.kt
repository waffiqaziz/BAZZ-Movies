package com.waffiq.bazz_movies.core.network.di

import com.waffiq.bazz_movies.core.network.domain.DebugConfigImpl
import com.waffiq.bazz_movies.core.network.domain.IDebugConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkConfigModule {

  @Singleton
  @Provides
  fun provideDebugConfig(): IDebugConfig = DebugConfigImpl()
}
