package com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible
import com.waffiq.bazz_movies.core.movie.utils.common.Constants.FADE_ALPHA

/**
 * Used as animation fade in and fade out
 */
object Animation {
  fun fadeOut(view: View, duration: Long = 300) {
    view.animate()
      .alpha(0f)
      .setDuration(duration)
      .setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          view.isVisible = false // Or View.INVISIBLE
        }
      })
  }

  fun fadeInAlpha50(view: View, duration: Long = 300) {
    view.apply {
      alpha = 0f
      isVisible = true
      animate()
        .alpha(FADE_ALPHA)
        .setDuration(duration)
        .setListener(null) // Remove listener if needed
    }
  }
}
