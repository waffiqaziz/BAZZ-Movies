package com.waffiq.bazz_movies.core.uihelper.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.color.red_matte

/**
 * Used to handle all snackbar warning
 */
object SnackBarManager {
  fun Context.toastShort(text: String) {
    Toast.makeText(
      applicationContext,
      HtmlCompat.fromHtml(
        text,
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Toast.LENGTH_SHORT
    ).show()
  }

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
