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
 * Utility object responsible for displaying Toast and Snackbar messages.
 *
 * This object provides helper methods to display:
 * - [toastShort], Short-duration toast messages with HTML formatting.
 * - [snackBarWarning], Warning Snackbar messages, which can be optionally anchored to another view.
 * It is used for consistent, easy-to-display alerts throughout the application.
 */
object SnackBarManager {

  /**
   * Displays a short-duration toast message with HTML content.
   *
   * This method creates and shows a toast message with a short duration.
   * The `text` parameter can contain HTML content, which will be converted and displayed.
   *
   * @param text The HTML-formatted text to be shown in the toast message.
   */
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

  /**
   * Displays a warning Snackbar message with an event-based message.
   * This method only displays the Snackbar if the message is valid (non-null and non-empty).
   * The Snackbar can optionally be anchored to a specific view (guideView).
   *
   * @param view The [View] to find the parent layout for the Snackbar.
   * @param guideView The optional [View] to anchor the Snackbar to (like a guide or button).
   * @param eventMessage The [Event] containing the message to be shown in the Snackbar.
   * @return The displayed [Snackbar], or null if no valid message is available.
   */
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

  /**
   * Displays a warning Snackbar message with a direct string message.
   * This method is similar to the one above, but it takes a direct string instead of an event.
   *
   * @param view The [View] to find the parent layout for the Snackbar.
   * @param guideView The optional [View] to anchor the Snackbar to (like a guide or button).
   * @param message The message to be shown in the Snackbar.
   * @return The displayed [Snackbar], or null if the message is invalid.
   */
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
