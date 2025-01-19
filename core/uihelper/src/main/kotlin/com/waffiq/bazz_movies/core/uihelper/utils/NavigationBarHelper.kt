package com.waffiq.bazz_movies.core.uihelper.utils

import android.content.Context
import android.os.Build
import android.view.Window
import androidx.core.content.ContextCompat
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_1000
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_900

object NavigationBarHelper {

  fun Context.setNavigationBarColor(window: Window) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
      @Suppress("DEPRECATION")
      window.navigationBarColor = ContextCompat.getColor(this, gray_1000)
    }
  }

  fun Context.setNavigationBarColorGrayed(window: Window) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
      @Suppress("DEPRECATION")
      window.navigationBarColor = ContextCompat.getColor(this, gray_900)
    }
  }
}