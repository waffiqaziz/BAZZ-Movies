package com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.appbar.AppBarLayout

object ActionBarBehavior {

  @Suppress("DEPRECATION")
  fun Window.transparentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      setDecorFitsSystemWindows(false)
    } else {
      // This flag is deprecated in API 30 (Android R), but necessary for older versions
      decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    statusBarColor = Color.TRANSPARENT
  }

  fun handleOverHeightAppBar(appBarLayout: AppBarLayout) {
    ViewCompat.setOnApplyWindowInsetsListener(appBarLayout) { v, windowInsets ->
      val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = insets.top
        leftMargin = insets.left
        rightMargin = insets.right
      }
      WindowInsetsCompat.CONSUMED
    }
  }
}