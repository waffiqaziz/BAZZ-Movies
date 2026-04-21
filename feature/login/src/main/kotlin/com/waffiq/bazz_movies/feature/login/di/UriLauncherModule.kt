package com.waffiq.bazz_movies.feature.login.di

import com.waffiq.bazz_movies.feature.login.utils.openurl.UriLauncherImpl
import com.waffiq.bazz_movies.feature.login.utils.openurl.UriLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
fun interface UriLauncherModule {

  @Binds
  fun bindUriLauncher(impl: UriLauncherImpl): UriLauncher
}
