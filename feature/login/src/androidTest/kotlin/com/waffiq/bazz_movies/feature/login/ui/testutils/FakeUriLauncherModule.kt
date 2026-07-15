package com.waffiq.bazz_movies.feature.login.ui.testutils

import com.waffiq.bazz_movies.feature.login.di.UriLauncherModule
import com.waffiq.bazz_movies.feature.login.utils.openurl.UriLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [UriLauncherModule::class],
)
abstract class FakeUriLauncherModule {
  @Binds
  @Singleton
  abstract fun bindFakeUriLauncher(impl: FakeUriLauncher): UriLauncher
}
