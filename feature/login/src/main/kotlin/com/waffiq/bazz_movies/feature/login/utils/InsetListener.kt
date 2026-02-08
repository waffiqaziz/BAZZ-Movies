package com.waffiq.bazz_movies.feature.login.utils

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.waffiq.bazz_movies.feature.login.ui.LoginActivity.Companion.PADDING_RIGHT

/**
 * Utility object for handling window inset behavior, particularly to adjust padding
 * based on navigation mode (gesture or button) in landscape orientation.
 */
object InsetListener {

  /*
   * Sets a window insets listener on [view] if in landscape mode, adjusting right padding
   * based on navigation mode (gesture or 3-button).
   */
  fun Activity.applyWindowInsets(view: View): OnApplyWindowInsetsListener? {
    val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    return if (isLandscape) {
      val listener = OnApplyWindowInsetsListener { v, insets ->
        val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val isGestureNavigation = navBarInsets.right == 0

        if (!isGestureNavigation) {
          v.setPadding(v.paddingLeft, v.paddingTop, PADDING_RIGHT, v.paddingBottom)
        } else {
          v.setPadding(v.paddingLeft, v.paddingTop, 0, v.paddingBottom)
        }
        insets
      }
      ViewCompat.setOnApplyWindowInsetsListener(view, listener)
      listener
    } else {
      null
    }
  }
}
