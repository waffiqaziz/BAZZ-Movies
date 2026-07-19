package com.waffiq.bazz_movies.core.instrumentationtest

import com.waffiq.bazz_movies.core.utils.di.UriLauncherModule
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [UriLauncherModule::class],
)
interface BanishUriLauncherModule {
  // Empty on purpose. Used to block the production module.
}

@Module
@InstallIn(SingletonComponent::class)
object DefaultMockUriLauncherModule {

  @Provides
  @Singleton
  fun provideMockUriLauncher(): UriLauncher = mockk(relaxed = true)
}
