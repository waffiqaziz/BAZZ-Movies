package com.waffiq.bazz_movies.feature.login.ui.testutils

import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.feature.login.ui.LoginViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

  @Provides
  @Singleton
  fun provideMockNavigator(): INavigator = mockk(relaxed = true)

  @Provides
  @Singleton
  fun provideMockSnackbar(): ISnackbar = mockk(relaxed = true)

  @Provides
  @Singleton
  fun provideMockloginViewModel(): LoginViewModel = mockk(relaxed = true)
}
