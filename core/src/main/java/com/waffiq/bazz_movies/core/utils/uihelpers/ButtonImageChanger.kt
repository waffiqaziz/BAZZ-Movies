package com.waffiq.bazz_movies.core.utils.uihelpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_bookmark
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_bookmark_selected
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_hearth_selected

/**
 * Used to update the drawable images for the favorite and watchlist buttons.
 */
object ButtonImageChanger {
  fun changeBtnWatchlistBG(
    context: Context,
    btnWatchlist: ImageButton,
    isActivated: Boolean
  ) {
    if (isActivated && btnWatchlist.drawable.constantState ==
      ActivityCompat.getDrawable(context, ic_bookmark_selected)?.constantState
    ) {
      return
    }

    if (!isActivated && btnWatchlist.drawable.constantState ==
      ActivityCompat.getDrawable(context, ic_bookmark)?.constantState
    ) {
      return
    }

    val toRes = if (isActivated) ic_bookmark_selected else ic_bookmark
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
        btnWatchlist.setImageResource(toRes)
        ObjectAnimator.ofFloat(btnWatchlist, View.ALPHA, 0f, 1f).apply {
          duration = 150
          interpolator = AccelerateDecelerateInterpolator()
        }.start()
      }
    })

    set.start()
  }

  fun changeBtnFavoriteBG(
    context: Context,
    btnFavorite: ImageButton,
    isActivated: Boolean
  ) {
    if (isActivated && btnFavorite.drawable.constantState ==
      ActivityCompat.getDrawable(context, ic_hearth_selected)?.constantState
    ) {
      return
    }

    if (!isActivated && btnFavorite.drawable.constantState ==
      ActivityCompat.getDrawable(context, ic_hearth)?.constantState
    ) {
      return
    }

    val toRes = if (isActivated) ic_hearth_selected else ic_hearth
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
        btnFavorite.setImageResource(toRes)
        ObjectAnimator.ofFloat(btnFavorite, View.ALPHA, 0f, 1f).apply {
          duration = 150
          interpolator = AccelerateDecelerateInterpolator()
        }.start()
      }
    })

    set.start()
  }
}
