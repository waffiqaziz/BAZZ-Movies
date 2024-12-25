package com.waffiq.bazz_movies.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

  @DefaultDispatcher
  @Provides
  @Singleton
  fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

  @IoDispatcher
  @Provides
  @Singleton
  fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

  @MainDispatcher
  @Provides
  @Singleton
  fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher
