package com.waffiq.bazz_movies.feature.detail.utils.uihelpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton

/**
 * Used to update the drawable images for the favorite and watchlist buttons.
 */
object ButtonImageChanger {
  const val DURATION_SCALING = 300L
  const val DURATION_ALPHA = 150L
  const val SCALE_SIZE = 1.2f

  fun changeBtnAction(
    button: ImageButton,
    isActivated: Boolean,
    iconActive: Int,
    iconInactive: Int,
  ) {
    // select target icon
    val targetRes = if (isActivated) iconActive else iconInactive

    // set the tag
    val currentDrawableRes = button.tag as? Int
    if (currentDrawableRes == targetRes) return

    // update the tag with the new resource ID
    button.tag = targetRes

    val scaleXAnimator =
      ObjectAnimator.ofFloat(button, View.SCALE_X, 1.0f, SCALE_SIZE, 1.0f).apply {
        duration = DURATION_SCALING
        interpolator = AccelerateDecelerateInterpolator()
      }

    val scaleYAnimator =
      ObjectAnimator.ofFloat(button, View.SCALE_Y, 1.0f, SCALE_SIZE, 1.0f).apply {
        duration = DURATION_SCALING
        interpolator = AccelerateDecelerateInterpolator()
      }

    val alphaAnimator = ObjectAnimator.ofFloat(button, View.ALPHA, 1f, 0f).apply {
      duration = DURATION_ALPHA
      startDelay = DURATION_ALPHA // delay the alpha animation for smooth transition
      interpolator = AccelerateDecelerateInterpolator()
    }

    val set = AnimatorSet().apply {
      playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
    }

    set.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        button.setImageResource(targetRes)
        ObjectAnimator.ofFloat(button, View.ALPHA, 0f, 1f).apply {
          duration = DURATION_ALPHA
          interpolator = AccelerateDecelerateInterpolator()
        }.start()
      }
    })

    set.start()
  }
}
