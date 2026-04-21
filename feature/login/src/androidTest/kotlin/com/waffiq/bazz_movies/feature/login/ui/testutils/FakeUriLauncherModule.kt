package com.waffiq.bazz_movies.feature.login.ui.testutils

import com.waffiq.bazz_movies.feature.login.di.UriLauncherModule
import com.waffiq.bazz_movies.feature.login.utils.openurl.UriLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Suppress("unused")
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [UriLauncherModule::class]
)
@Module
fun interface FakeUriLauncherModule {

  @Binds
  fun bindFakeUriLauncher(impl: FakeUriLauncher): UriLauncher
}
