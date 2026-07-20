package com.waffiq.bazz_movies.core.instrumentationtest.module

import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockSnackbarModule {

  @Provides
  @Singleton
  fun provideMockSnackbar(): ISnackbar = mockk(relaxed = true)
}
