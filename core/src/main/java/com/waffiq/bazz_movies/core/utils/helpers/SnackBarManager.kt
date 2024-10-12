package com.waffiq.bazz_movies.core.utils.helpers

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.R.color.red_matte
import com.waffiq.bazz_movies.core.utils.common.Event

/**
 * Used to handle all snackbar warning
 */
object SnackBarManager {

  fun snackBarWarning(
    view: View,
    guideView: View?,
    eventMessage: Event<String>
  ): Snackbar? {
    val message = eventMessage.getContentIfNotHandled()?.takeIf { it.isNotEmpty() }

    return if (message != null && view.isAttachedToWindow) {
      Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        guideView?.let { anchorView = it } // Check if guideView is non-null
        setBackgroundTint(ContextCompat.getColor(context, red_matte))
        show()
      }
    } else {
      null
    }
  }

  fun snackBarWarning(
    view: View,
    guideView: View?,
    message: String?
  ): Snackbar? {
    return if (!message.isNullOrEmpty() && view.isAttachedToWindow) {
      Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        guideView?.let { anchorView = it } // Check if guideView is non-null
        setBackgroundTint(ContextCompat.getColor(context, red_matte))
        show()
      }
    } else {
      null
    }
  }
}
