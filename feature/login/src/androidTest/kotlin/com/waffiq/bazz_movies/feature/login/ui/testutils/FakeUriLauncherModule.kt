package com.waffiq.bazz_movies.feature.login.ui.testutils

import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FakeUriLauncherModule {

  @Binds
  @Singleton
  fun bindFakeUriLauncher(impl: FakeUriLauncher): UriLauncher
}
