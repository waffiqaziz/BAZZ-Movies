package com.waffiq.bazz_movies.core.uihelper.utils

import android.R.anim.fade_out
import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.text.LineBreaker
import android.os.Build
import android.text.Layout
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_VERY_LONG
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_1000
import kotlin.collections.forEach

object Helpers {

  @Suppress("WrongConstant")
  fun justifyTextView(textView: TextView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      textView.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      textView.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
    }
  }

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
        animateColorChange(appBarLayout, fromColor, toColor, percentage)
        setStatusBarColorWithAnimation(window, fromColor, toColor, percentage)
      }
    )
  }

  private fun animateColorChange(
    appBarLayout: AppBarLayout,
    fromColor: Int,
    toColor: Int,
    percentage: Float
  ) {
    // Calculate the adjusted progress based on the percentage scrolled
    val adjustedProgress = percentage.coerceIn(0f, 1f) // Ensure the progress is between 0 and 1

    // Calculate the interpolated color based on the adjusted progress
    val interpolatedColor = ArgbEvaluator().evaluate(adjustedProgress, fromColor, toColor) as Int

    // Set the interpolated color as the background color of the AppBarLayout
    appBarLayout.setBackgroundColor(interpolatedColor)
  }

  private fun setStatusBarColorWithAnimation(
    window: Window,
    fromColor: Int,
    toColor: Int,
    percentage: Float
  ) {
    val interpolatedColor =
      ArgbEvaluator().evaluate(percentage.coerceIn(0f, 1f), fromColor, toColor) as Int
    window.statusBarColor = interpolatedColor
  }

  fun animFadeOutLong(context: Context): Animation {
    val animation = AnimationUtils.loadAnimation(context, fade_out)
    animation.duration = DEBOUNCE_VERY_LONG
    return animation
  }

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
