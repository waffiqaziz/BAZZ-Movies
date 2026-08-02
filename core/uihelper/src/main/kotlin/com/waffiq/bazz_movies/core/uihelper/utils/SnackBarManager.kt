package com.waffiq.bazz_movies.core.uihelper.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.designsystem.R.color.red_matte

/**
 * Utility object responsible for displaying Toast and Snackbar messages.
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
    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
  }

  /**
   * Displays a warning Snackbar message with a direct string message.
   * This method is similar to the one above, but it takes a direct string instead of an event.
   *
   * @param view The [View] to find the parent layout for the Snackbar.
   * @param message The message to be shown in the Snackbar.
   * @param anchorView The optional [View] to anchor the Snackbar to (like a guide or button).
   * @return The displayed [Snackbar], or null if the message is invalid.
   */
  @Suppress("TooGenericExceptionCaught")
  fun snackBarWarning(
    view: View,
    anchorView: View? = null,
    message: String,
  ): Snackbar? {
    return try {
      if (!view.isAttachedToWindow || message.isEmpty() || message.isBlank()) return null

      Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
        anchorView?.let { this.anchorView = it }
        setBackgroundTint(ContextCompat.getColor(view.context, red_matte))
        show()
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error creating snackbar", e)
      null
    }
  }

  private const val TAG = "SnackBarManager"
}
