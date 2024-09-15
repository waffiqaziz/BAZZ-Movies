package com.waffiq.bazz_movies.utils.uihelpers

import android.R.color.transparent
import android.animation.ArgbEvaluator
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.R.color.gray

class ScrollActionBarBehavior private constructor(
  private val appBarLayout: AppBarLayout,
  private val nestedScrollView: NestedScrollView
) {

  companion object {
    fun setupScrollActionBarBehavior(
      context: Context,
      appBarLayout: AppBarLayout,
      nestedScrollView: NestedScrollView
    ) {
      val fromColor = ContextCompat.getColor(context, transparent)
      val toColor = ContextCompat.getColor(context, gray)
      val behavior = ScrollActionBarBehavior(appBarLayout, nestedScrollView)
      behavior.setupScrollListener(fromColor, toColor)
    }
  }

  private fun setupScrollListener(fromColor: Int, toColor: Int) {
    nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
      val maxScroll = nestedScrollView.getChildAt(0).height - nestedScrollView.height
      val percentage = if (maxScroll > 0) scrollY.toFloat() / maxScroll.toFloat() else 0f
      animateColorChange(fromColor, toColor, percentage)
    }
  }

  private fun animateColorChange(fromColor: Int, toColor: Int, percentage: Float) {
    val adjustedProgress = percentage.coerceIn(0f, 1f)
    val interpolatedColor = ArgbEvaluator().evaluate(adjustedProgress, fromColor, toColor) as Int
    appBarLayout.setBackgroundColor(interpolatedColor)
  }
}
