package com.waffiq.bazz_movies.core.uihelper.utils

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_1000
import com.waffiq.bazz_movies.core.uihelper.utils.ScrollActionBarUtils.scrollActionBarBehavior

/**
 * A utility object that provides scroll helper functions using [scrollActionBarBehavior],
 */
object ScrollActionBarUtils {

  /**
   * Sets up a scroll listener to change the background color of the AppBarLayout based on the
   * scroll position of the [NestedScrollView].
   *
   * This creates a smooth color transition effect when the user scrolls the content.
   *
   * @param appBarLayout The [AppBarLayout] whose background color will change as the user scrolls.
   * @param nestedScrollView The [NestedScrollView] that triggers the scroll behavior.
   */
  fun Context.scrollActionBarBehavior(
    appBarLayout: AppBarLayout,
    nestedScrollView: NestedScrollView,
  ) {
    val fromColor = ContextCompat.getColor(this, android.R.color.transparent)
    val toColor = ContextCompat.getColor(this, gray_1000)
    val argbEvaluator = ArgbEvaluator()
    val gradientDrawable = GradientDrawable()

    nestedScrollView.setOnScrollChangeListener(
      NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
        val maxScroll = nestedScrollView.getChildAt(0).height - nestedScrollView.height
        val percentage = if (maxScroll > 0) scrollY.toFloat() / maxScroll.toFloat() else 0f
        val interpolatedColor =
          argbEvaluator.evaluate(percentage.coerceIn(0f, 1f), fromColor, toColor) as Int

        // change background appbar layout using drawable
        val drawable = gradientDrawable.apply {
          shape = GradientDrawable.RECTANGLE
          cornerRadius = 0f
          setColor(interpolatedColor)
        }
        appBarLayout.background = drawable
      },
    )
  }
}
