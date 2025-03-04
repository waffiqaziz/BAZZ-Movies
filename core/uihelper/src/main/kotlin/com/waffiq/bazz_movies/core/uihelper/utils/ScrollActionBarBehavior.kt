package com.waffiq.bazz_movies.core.uihelper.utils

import android.animation.ArgbEvaluator
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_1000

/**
 * A utility object that provides scroll helper functions using [scrollActionBarBehavior],
 */
object ScrollActionBarBehavior {
  /**
   * Sets up a scroll listener to change the background color of the AppBarLayout and the status bar color
   * based on the scroll position of the [NestedScrollView].
   * This creates a smooth color transition effect when the user scrolls the content.
   *
   * @param window The [Window] where the status bar color will be modified.
   * @param appBarLayout The [AppBarLayout] whose background color will change as the user scrolls.
   * @param nestedScrollView The [NestedScrollView] that triggers the scroll behavior.
   */
  fun Context.scrollActionBarBehavior(
    window: Window,
    appBarLayout: AppBarLayout,
    nestedScrollView: NestedScrollView
  ) {
    val fromColor = ContextCompat.getColor(this, android.R.color.transparent)
    val toColor = ContextCompat.getColor(this, gray_1000)

    nestedScrollView.setOnScrollChangeListener(
      NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
        val maxScroll = nestedScrollView.getChildAt(0).height - nestedScrollView.height
        val percentage = if (maxScroll > 0) scrollY.toFloat() / maxScroll.toFloat() else 0f
        setAppBarColor(appBarLayout, fromColor, toColor, percentage)
        setStatusBarColorWithAnimation(window, fromColor, toColor, percentage)
      }
    )
  }

  /**
   * Animates the color change of the AppBarLayout based on the scroll percentage.
   *
   * @param appBarLayout The [AppBarLayout] whose background color will change.
   * @param fromColor The starting color of the animation.
   * @param toColor The ending color of the animation.
   * @param percentage The scroll percentage that dictates the color interpolation.
   */
  private fun setAppBarColor(
    appBarLayout: AppBarLayout,
    fromColor: Int,
    toColor: Int,
    percentage: Float
  ) {
    // calculate the adjusted progress based on the percentage scrolled
    val adjustedProgress = percentage.coerceIn(0f, 1f) // Ensure the progress is between 0 and 1

    // calculate the interpolated color based on the adjusted progress
    val interpolatedColor = ArgbEvaluator().evaluate(adjustedProgress, fromColor, toColor) as Int

    // set the interpolated color as the background color of the AppBarLayout
    appBarLayout.setBackgroundColor(interpolatedColor)
  }

  /**
   * Animates the status bar color change based on the scroll percentage.
   *
   * @param window The [Window] whose status bar color will be animated.
   * @param fromColor The starting color of the animation.
   * @param toColor The ending color of the animation.
   * @param percentage The scroll percentage that dictates the color interpolation.
   */
  private fun Context.setStatusBarColorWithAnimation(
    window: Window,
    fromColor: Int,
    toColor: Int,
    percentage: Float
  ) {
    val interpolatedColor =
      ArgbEvaluator().evaluate(percentage.coerceIn(0f, 1f), fromColor, toColor) as Int

    // set status bar color based on API level
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
      // For Android 14 and below
      @Suppress("DEPRECATION")
      window.statusBarColor = interpolatedColor
    } else { // Android 15 and up
      val insetsController = WindowInsetsControllerCompat(window, window.decorView)
      insetsController.isAppearanceLightStatusBars = isLightColor(interpolatedColor)

      // add a colored background to the status bar area
      val rootView = window.decorView as ViewGroup
      val statusBarBackgroundView = View(this).apply {
        setBackgroundColor(interpolatedColor)
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          window.getStatusBarHeight()
        )
      }

      // remove existing status bar background if present
      val existingBackground = rootView.findViewWithTag<View>("statusBarBackground")
      if (existingBackground != null) rootView.removeView(existingBackground)

      // add the new status bar background view
      statusBarBackgroundView.tag = "statusBarBackground"
      rootView.addView(statusBarBackgroundView)
    }
  }

  @Suppress("MagicNumber")
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal fun isLightColor(color: Int): Boolean {
    val darkness = 1 - (
      0.299 * ((color shr 16 and 0xFF) / 255.0) +
        0.587 * ((color shr 8 and 0xFF) / 255.0) +
        0.114 * ((color and 0xFF) / 255.0)
      )
    return darkness < 0.5
  }

  // helper to get the status bar height
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal fun Window.getStatusBarHeight(): Int {
    val insets = ViewCompat.getRootWindowInsets(decorView) ?: return 0
    return insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
  }
}
