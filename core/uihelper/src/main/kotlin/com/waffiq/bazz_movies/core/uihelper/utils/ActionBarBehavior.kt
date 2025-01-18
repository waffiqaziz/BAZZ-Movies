package com.waffiq.bazz_movies.core.uihelper.utils

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.appbar.AppBarLayout

/**
 * A utility object that provides functions for modifying the appearance and behavior of the status bar
 * and handling app bar layout in an Android application.
 *
 * This object includes:
 * - [handleOverHeightAppBar], to adjust the app bar layout to handle system insets, ensuring proper
 *   display on different device configurations.
 */
object ActionBarBehavior {

  fun AppBarLayout.handleOverHeightAppBar() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
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
