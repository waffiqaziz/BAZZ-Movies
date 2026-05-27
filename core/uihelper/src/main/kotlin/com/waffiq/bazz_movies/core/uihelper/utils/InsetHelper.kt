package com.waffiq.bazz_movies.core.uihelper.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

object InsetHelper {

  fun View.setupWindowInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
      val insets = windowInsets.getInsets(
        WindowInsetsCompat.Type.systemBars()
          or WindowInsetsCompat.Type.displayCutout(),
      )
      v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = insets.left
        rightMargin = insets.right
      }
      windowInsets
    }
  }
}
