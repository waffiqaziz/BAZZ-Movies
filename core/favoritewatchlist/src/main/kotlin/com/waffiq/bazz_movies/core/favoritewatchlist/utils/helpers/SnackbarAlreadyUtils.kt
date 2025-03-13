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
   * Displays a Snackbar indicating that an item is already in the watchlist.
   * The message is styled with bold text and a predefined message that includes the item's name.
   *
   * @param context The context in which the Snackbar is displayed.
   * @param view The view to associate with the Snackbar.
   * @param viewGuide A view to anchor the Snackbar to.
   * @param eventMessage The message event to be displayed in the Snackbar.
   * @return The Snackbar instance, or null if no content is available to display.
   */
  fun snackBarAlreadyWatchlist(
    context: Context,
    view: View,
    viewGuide: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val result = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      HtmlCompat.fromHtml(
        "<b>$result</b> " + ContextCompat.getString(context, already_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

  /**
   * Displays a Snackbar indicating that an item is already marked as a favorite.
   * The message is styled with bold text and a predefined message that includes the item's name.
   *
   * @param context The context in which the Snackbar is displayed.
   * @param view The view to associate with the Snackbar.
   * @param viewGuide A view to anchor the Snackbar to.
   * @param eventMessage The message event to be displayed in the Snackbar.
   * @return The Snackbar instance, or null if no content is available to display.
   */
  fun snackBarAlreadyFavorite(
    context: Context,
    view: View,
    viewGuide: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val result = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      HtmlCompat.fromHtml(
        "<b>$result</b> " + ContextCompat.getString(context, already_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }
}
