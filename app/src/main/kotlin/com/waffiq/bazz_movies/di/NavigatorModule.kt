package com.waffiq.bazz_movies.di

import com.waffiq.bazz_movies.navigation.AppNavigator
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigatorModule {

  @Provides
  @Singleton
  fun provideNavigator(): INavigator = AppNavigator()
}
