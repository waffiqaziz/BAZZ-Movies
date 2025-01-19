package com.waffiq.bazz_movies.core.uihelper.utils

import android.R.anim.fade_out
import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_1000
import kotlin.collections.forEach

/**
 * A utility object that provides various helper functions for UI customization and behavior in an
 * Android app. These helpers include [justifyTextView], [scrollActionBarBehavior], [setAppBarColor],
 * [setStatusBarColorWithAnimation], [animFadeOutLong], [setupRecyclerViewsWithSnap], and
 * [setupRecyclerViewsWithSnapGridLayout].
 */
object Helpers {

  /**
   * Justifies the text in a [TextView] based on the Android version. It uses
   * `LineBreaker.JUSTIFICATION_MODE_INTER_WORD` on Android Q and above, and
   * `Layout.JUSTIFICATION_MODE_INTER_WORD` on Android O to P.
   *
   * @param textView The [TextView] whose text will be justified.
   */
  @Suppress("WrongConstant")
  fun justifyTextView(textView: TextView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      textView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
    }
  }

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

  private fun isLightColor(color: Int): Boolean {
    val darkness = 1 - (0.299 * ((color shr 16 and 0xFF) / 255.0) +
      0.587 * ((color shr 8 and 0xFF) / 255.0) +
      0.114 * ((color and 0xFF) / 255.0))
    return darkness < 0.5
  }

  // helper to get the status bar height
  private fun Window.getStatusBarHeight(): Int {
    val insets = ViewCompat.getRootWindowInsets(decorView) ?: return 0
    return insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
  }

  /**
   * Returns a long-duration fade-out animation.
   *
   * @param context The [Context] used to load the animation.
   * @return The [Animation] object with the specified duration.
   */
  fun animFadeOutLong(context: Context): Animation {
    val animation = AnimationUtils.loadAnimation(context, fade_out)
    animation.duration = DEBOUNCE_VERY_LONG
    return animation
  }

  /**
   * Sets up a list of RecyclerViews with horizontal snap behavior using a [CustomSnapHelper].
   * This enables smooth snapping of items when the user scrolls the RecyclerView.
   *
   * @param recyclerViews A list of [RecyclerView]s to apply the snap behavior.
   * @param layoutManager An optional [LinearLayoutManager] for configuring the layout of the RecyclerView.
   */
  fun setupRecyclerViewsWithSnap(
    recyclerViews: List<RecyclerView>,
    layoutManager: LinearLayoutManager? = null
  ) {
    recyclerViews.forEach { recyclerView ->

      // Safely attach SnapHelper
      if (recyclerView.onFlingListener == null) {
        recyclerView.layoutManager = layoutManager ?: LinearLayoutManager(
          recyclerView.context, LinearLayoutManager.HORIZONTAL, false
        )
        CustomSnapHelper().attachToRecyclerView(recyclerView)
      }
    }
  }

  /**
   * Sets up a list of RecyclerViews with horizontal snap behavior using a [GridLayoutManager].
   * This enables snapping with a grid layout configuration.
   *
   * @param n The number of columns in the grid layout (default is 2).
   * @param recyclerViews A list of [RecyclerView]s to apply the snap behavior.
   * @param layoutManager An optional [LinearLayoutManager] for configuring the layout of the RecyclerView.
   */
  fun setupRecyclerViewsWithSnapGridLayout(
    n: Int = 2,
    recyclerViews: List<RecyclerView>,
    layoutManager: LinearLayoutManager? = null
  ) {
    recyclerViews.forEach { recyclerView ->

      // Safely attach SnapHelper
      if (recyclerView.onFlingListener == null) {
        recyclerView.layoutManager =
          layoutManager ?: GridLayoutManager(
            recyclerView.context,
            n,
            GridLayoutManager.HORIZONTAL,
            false
          )
        CustomSnapHelper().attachToRecyclerView(recyclerView)
      }
    }
  }
}
