package com.waffiq.bazz_movies.core.instrumentationtest.module

import com.waffiq.bazz_movies.navigation.INavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockNavigatorModule {

  @Provides
  @Singleton
  fun provideMockNavigator(): INavigator = mockk(relaxed = true)
}
