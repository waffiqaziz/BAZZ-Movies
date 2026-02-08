package com.waffiq.bazz_movies.core.uihelper.utils

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.appbar.AppBarLayout

/**
 * A utility object that provides functions for modifying the appearance and behavior status bar
 * and handling appbar layout.
 *
 * This object includes:
 * - [handleOverHeightAppBar], to adjust the appbar layout to handle system insets, ensuring proper
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
