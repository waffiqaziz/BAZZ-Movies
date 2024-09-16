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
    guideView: View?,
    eventMessage: Event<String>
  ): Snackbar? {
    val message = eventMessage.getContentIfNotHandled()?.takeIf { it.isNotEmpty() }

    return if (message != null && view.isAttachedToWindow) {
      Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        guideView?.let { anchorView = it } // Check if guideView is non-null
        setBackgroundTint(ContextCompat.getColor(context, R.color.red_matte))
        show()
      }
    } else {
      null
    }
  }

  fun snackBarWarning(
    context: Context,
    view: View,
    guideView: View?,
    message: String?
  ): Snackbar? {
    return if (!message.isNullOrEmpty() && view.isAttachedToWindow) {
      Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        guideView?.let { anchorView = it } // Check if guideView is non-null
        setBackgroundTint(ContextCompat.getColor(context, R.color.red_matte))
        show()
      }
    } else {
      null
    }
  }
}
