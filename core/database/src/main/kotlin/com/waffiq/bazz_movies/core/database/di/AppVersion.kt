package com.waffiq.bazz_movies.core.database.di

import com.waffiq.bazz_movies.core.database.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppVersion

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @AppVersion
  fun provideAppVersion(): String = BuildConfig.APP_VERSION
}
