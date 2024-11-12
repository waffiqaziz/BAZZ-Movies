package com.waffiq.bazz_movies.feature.detail.utils.uihelpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_bookmark
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_bookmark_selected
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_hearth_selected

/**
 * Used to update the drawable images for the favorite and watchlist buttons.
 */
object ButtonImageChanger {
  fun changeBtnWatchlistBG(
    btnWatchlist: ImageButton,
    isActivated: Boolean
  ) {
    // Determine the resource ID for the target drawable based on isActivated state
    val targetRes = if (isActivated) ic_bookmark_selected else ic_bookmark

    // Check if btnFavorite already has the correct resource by using the tag
    val currentDrawableRes = btnWatchlist.tag as? Int
    if (currentDrawableRes == targetRes) return

    // Update the tag with the new resource ID
    btnWatchlist.tag = targetRes

    val scaleXAnimator =
      ObjectAnimator.ofFloat(btnWatchlist, View.SCALE_X, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val scaleYAnimator =
      ObjectAnimator.ofFloat(btnWatchlist, View.SCALE_Y, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val alphaAnimator = ObjectAnimator.ofFloat(btnWatchlist, View.ALPHA, 1f, 0f).apply {
      duration = 150
      startDelay = 150 // Delay the alpha animation for smooth transition
      interpolator = AccelerateDecelerateInterpolator()
    }

    val set = AnimatorSet().apply {
      playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
    }

    set.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        btnWatchlist.setImageResource(targetRes)
        ObjectAnimator.ofFloat(btnWatchlist, View.ALPHA, 0f, 1f).apply {
          duration = 150
          interpolator = AccelerateDecelerateInterpolator()
        }.start()
      }
    })

    set.start()
  }

  fun changeBtnFavoriteBG(
    btnFavorite: ImageButton,
    isActivated: Boolean
  ) {

    // Determine the resource ID for the target drawable based on isActivated state
    val targetRes = if (isActivated) ic_hearth_selected else ic_hearth

    // Check if btnFavorite already has the correct resource by using the tag
    val currentDrawableRes = btnFavorite.tag as? Int
    if (currentDrawableRes == targetRes) return

    // Update the tag with the new resource ID
    btnFavorite.tag = targetRes

    val scaleXAnimator =
      ObjectAnimator.ofFloat(btnFavorite, View.SCALE_X, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val scaleYAnimator =
      ObjectAnimator.ofFloat(btnFavorite, View.SCALE_Y, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val alphaAnimator = ObjectAnimator.ofFloat(btnFavorite, View.ALPHA, 1f, 0f).apply {
      duration = 150
      startDelay = 150 // Delay the alpha animation for smooth transition
      interpolator = AccelerateDecelerateInterpolator()
    }

    val set = AnimatorSet().apply {
      playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
    }

    set.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        btnFavorite.setImageResource(targetRes)
        ObjectAnimator.ofFloat(btnFavorite, View.ALPHA, 0f, 1f).apply {
          duration = 150
          interpolator = AccelerateDecelerateInterpolator()
        }.start()
      }
    })

    set.start()
  }
}
