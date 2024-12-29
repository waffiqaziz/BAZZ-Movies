package com.waffiq.bazz_movies.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.waffiq.bazz_movies.R.id.bottom_navigation
import com.waffiq.bazz_movies.core.uihelper.ISnackbar
import com.waffiq.bazz_movies.snackbar.AppSnackbar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object SnackbarModule {

  @Provides
  fun provideAppCompatActivity(@ActivityContext context: Context): AppCompatActivity {
    return context as AppCompatActivity
  }

  @Provides
  fun provideBottomNavigationView(activity: AppCompatActivity): BottomNavigationView {
    return activity.findViewById(bottom_navigation)
  }

  @Provides
  fun provideNavigator(bottomNavigationView: BottomNavigationView): ISnackbar {
    return AppSnackbar(bottomNavigationView)
  }
}
