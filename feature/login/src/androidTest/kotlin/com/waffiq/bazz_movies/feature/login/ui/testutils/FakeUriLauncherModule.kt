package com.waffiq.bazz_movies.feature.login.ui.testutils

import com.waffiq.bazz_movies.core.instrumentationtest.DefaultMockUriLauncherModule
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [DefaultMockUriLauncherModule::class],
)
interface FakeUriLauncherModule {

  @Binds
  @Singleton
  fun bindFakeUriLauncher(impl: FakeUriLauncher): UriLauncher
}
