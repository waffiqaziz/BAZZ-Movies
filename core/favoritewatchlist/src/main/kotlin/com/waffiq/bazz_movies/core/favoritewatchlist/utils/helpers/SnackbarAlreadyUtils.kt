package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.string.already_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.already_watchlist

object SnackbarAlreadyUtils {
  /**
   * Displays a Snackbar indicating that an item is already in the favorite or watchlist.
   * The message is styled with bold text and a predefined message that includes the item's name.
   *
   * @param context The context in which the Snackbar is displayed.
   * @param view The view to associate with the Snackbar.
   * @param viewGuide A view to anchor the Snackbar to.
   * @param eventMessage The message event to be displayed in the Snackbar.
   * @param isFavorite As flag to check if the item in favorite or watchlist
   * @return The Snackbar instance, or null if no content is available to display.
   */
  fun snackBarAlready(
    context: Context,
    view: View,
    viewGuide: View,
    eventMessage: Event<String>,
    isFavorite: Boolean,
  ): Snackbar? {
    val result = eventMessage.getContentIfNotHandled() ?: return null
    val message = if (isFavorite) {
      HtmlCompat.fromHtml(
        "<b>$result</b> " + ContextCompat.getString(context, already_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      )
    } else {
      HtmlCompat.fromHtml(
        "<b>$result</b> " + ContextCompat.getString(context, already_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      )
    }
    val mSnackbar = Snackbar.make(
      view,
      message,
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }
}
