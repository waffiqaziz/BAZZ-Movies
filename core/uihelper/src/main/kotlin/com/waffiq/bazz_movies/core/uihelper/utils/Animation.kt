package com.waffiq.bazz_movies.core.uihelper.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible

/**
 * Utility object providing simple animations for fading in and fading out views.
 *
 * These animations used for transitions, such as showing or hiding views smoothly.
 * - [fadeOut] method will animate the view's alpha to 0.
 * - [fadeInAlpha50] method animates the view's alpha to 50% opacity.
 */
object Animation {

  private const val DEFAULT_DURATION = 300L
  private const val FADE_ALPHA = 0.5f

  /**
   * Fades out the given view by animating its alpha to 0, making it invisible.
   * The view will become invisible (`View.INVISIBLE`) once the animation ends, ensuring that
   * it no longer occupies space in the layout.
   *
   * @param view The [View] that will be faded out.
   * @param duration The duration of the fade-out animation, in milliseconds. Defaults to 300ms.
   */
  fun fadeOut(view: View, duration: Long = DEFAULT_DURATION) {
    val safeDuration = if (duration < 0) DEFAULT_DURATION else duration
    view.animate()
      .alpha(0f)
      .setDuration(safeDuration)
      .setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          view.isVisible = false
        }
      })
  }

  /**
   * Fades in the given view by animating its alpha from 0 to 50% opacity.
   * This method ensures that the view is initially invisible and then fades in to 50% opacity.
   *
   * @param view The [View] that will be faded in.
   * @param duration The duration of the fade-in animation, in milliseconds. Defaults to 300ms.
   */
  fun fadeInAlpha50(view: View, duration: Long = DEFAULT_DURATION) {
    val safeDuration = if (duration < 0) DEFAULT_DURATION else duration
    view.apply {
      alpha = 0f
      isVisible = true
      animate()
        .alpha(FADE_ALPHA)
        .setDuration(safeDuration)
        .setListener(null) // remove listener
    }
  }
}
