package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding

/**
 * Helper class for managing UI adjustments related to navigation modes (button navigation vs gesture navigation).
 *
 * This class provides functionality to apply bottom padding to views when button navigation is enabled. It
 * checks the device's navigation mode and adjusts the layout accordingly, ensuring that the view does not
 * overlap with system bars or navigation buttons.
 *
 * The core functionality includes:
 * - [isButtonNavigationEnabled] to checking whether button navigation or gesture navigation is enabled on the device.
 * - [applyBottomPaddingForButtonNavigation], applying appropriate padding to the view when button navigation is active.
 */
object GestureHelper {

  /**
   * Extension function for [Activity] that applies bottom padding to a given [View] if button navigation
   * is enabled. This is particularly useful for handling UI adjustments on devices with system navigation bars.
   *
   * If button navigation is enabled (and not gesture navigation), this method will apply the bottom padding
   * based on the system bars insets, ensuring that the view does not overlap with the navigation buttons.
   *
   * @param view The [View] that should receive the bottom padding when button navigation is enabled.
   */
  fun Activity.addPaddingWhenNavigationEnable(view: View) {
    // check if button navigation is enabled (false means gesture navigation is enabled)
    if (isButtonNavigationEnabled()) {
      applyBottomPaddingForButtonNavigation(view)
    }
  }

  /**
   * Determines whether button navigation is enabled or not.
   *
   * For devices with API 30 (Android 11) and higher, this method checks the system bars behavior to determine
   * if gesture navigation is enabled. On older devices (API 29 or lower), it checks system settings to determine
   * if button navigation is enabled.
   *
   * @return `true` if button navigation is enabled, `false` if gesture navigation is enabled.
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun Activity.isButtonNavigationEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      // On API 30+ (Android 11 and later), check if gesture navigation is active using system bar behavior
      val controller = WindowInsetsControllerCompat(window, window.decorView)

      /**
       * The system bar behavior helps determine the type of navigation mode.
       * - `BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE` indicates that gesture navigation is enabled.
       * - Any other behavior (e.g., `BEHAVIOR_SHOW_BARS_BY_TOUCH`) means button navigation is enabled.
       */
      controller.systemBarsBehavior != WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
      // fallback for API 29 (Android 10) or lower, using secure settings to detect gesture navigation
      try {
        val gestureMode = Settings.Secure.getInt(contentResolver, "secure_gesture_navigation")
        gestureMode != 1 // returns true if button navigation is enabled, false for gesture navigation
      } catch (e: Settings.SettingNotFoundException) {
        Log.e("GestureHelper", e.toString())
        true // default to true (button navigation) if the setting isn't found
      }
    }
  }

  /**
   * Applies bottom padding to the view if button navigation is enabled. This function is invoked when
   * button navigation is detected, ensuring that the view's content does not overlap with the system navigation
   * bar (button area).
   *
   * This method uses `ViewCompat.setOnApplyWindowInsetsListener` to adjust the padding dynamically based on
   * the system insets.
   *
   * @param view The [View] to which the bottom padding will be applied.
   */
  private fun applyBottomPaddingForButtonNavigation(view: View) {
    // use the system insets to apply the appropriate bottom padding
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
      val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      // apply the bottom padding based on system bars (navigation bar area)
      v.updatePadding(bottom = systemBarInsets.bottom)
      insets // return the insets for further processing
    }
  }
}
