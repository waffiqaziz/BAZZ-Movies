package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.navigation.INavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockNavigatorModule {

  @Provides
  @Singleton
  fun provideMockNavigator(): INavigator =
    mockk<INavigator>(relaxed = true).apply {
      every { openAboutActivity(any()) } just Runs
      every { openDetails(any(), any()) } just Runs
      every { openList(any(), any()) } just Runs
      every { openLoginActivity(any()) } just Runs
      every { openMainActivity(any()) } just Runs
      every { openPersonDetails(any(), any()) } just Runs
    }
}
