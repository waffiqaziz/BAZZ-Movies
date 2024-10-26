package com.waffiq.bazz_movies.core.utils.helpers.uihelpers

import android.app.Activity
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding

object GestureHelper {

  fun Activity.addPaddingWhenNavigationEnable(view: View) {
    if (isButtonNavigationEnabled()) {
      applyBottomPaddingForButtonNavigation(view)
    }
  }


  private fun Activity.isButtonNavigationEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      // On API 30+, check if gesture navigation is active using system bar behavior
      val controller = WindowInsetsControllerCompat(window, window.decorView)
      controller.systemBarsBehavior != WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
      // Fallback for API 29: Use secure settings to detect gesture navigation
      try {
        val gestureMode = Settings.Secure.getInt(contentResolver, "secure_gesture_navigation")
        gestureMode != 1  // Returns true if button navigation is enabled
      } catch (e: Settings.SettingNotFoundException) {
        true  // Default to true if setting isn't found
      }
    }
  }

  private fun applyBottomPaddingForButtonNavigation(view: View) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
      val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.updatePadding(bottom = systemBarInsets.bottom)  // Apply bottom padding if using button navigation
      insets
    }
  }
}