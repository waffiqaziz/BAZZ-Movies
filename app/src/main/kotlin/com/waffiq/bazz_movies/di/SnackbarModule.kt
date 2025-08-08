package com.waffiq.bazz_movies.di

import android.content.Context
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.snackbar.AppSnackbarManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object SnackbarModule {

  @Provides
  fun provideSnackbar(@ActivityContext context: Context): ISnackbar =
    AppSnackbarManager(context)
}
