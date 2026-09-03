package com.waffiq.bazz_movies

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    AppCompatDelegate.setApplicationLocales(
      LocaleListCompat.forLanguageTags("en"),
    )
  }
}
