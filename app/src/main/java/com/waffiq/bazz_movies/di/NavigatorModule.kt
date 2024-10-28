package com.waffiq.bazz_movies.di


import com.waffiq.bazz_movies.navigation.Navigator
import com.waffiq.bazz_movies.navigation.AppNavigator
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
  fun provideNavigator(): Navigator = AppNavigator()
}