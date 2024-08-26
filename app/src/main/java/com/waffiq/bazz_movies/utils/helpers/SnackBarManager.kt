package com.waffiq.bazz_movies.utils.helpers

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.utils.common.Event

object SnackBarManager {

  fun snackBarWarning(
    context: Context,
    view: View,
    guideView: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val message = eventMessage.getContentIfNotHandled()?.takeIf { it.isNotEmpty() } ?: return null

    if (view.isAttachedToWindow) {
      val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setAnchorView(guideView)

      mSnackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.red_matte))
      mSnackbar.show()
      return mSnackbar
    }
    return null
  }
}