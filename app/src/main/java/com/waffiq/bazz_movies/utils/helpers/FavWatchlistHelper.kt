package com.waffiq.bazz_movies.utils.helpers

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.utils.common.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object FavWatchlistHelper {
  fun titleHandler(item: ResultItem): String {
    return item.name ?: item.title ?: item.originalTitle ?: "Item"
  }

  fun snackBarWarning(
    context: Context,
    view: View,
    guideView: View,
    eventMessage: Event<String>
  ): Snackbar? {
    val message = eventMessage.getContentIfNotHandled() ?: return null
    val mSnackbar = Snackbar.make(
      view,
      message.ifEmpty { ContextCompat.getString(context, R.string.unknown_error) },
      Snackbar.LENGTH_SHORT
    ).setAnchorView(guideView)

    val snackbarView = mSnackbar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red_matte))
    if (message.isNotEmpty()) mSnackbar.show()
    return mSnackbar
  }

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
        "<b>${result}</b> " + ContextCompat.getString(context, R.string.already_watchlist),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

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
        "<b>${result}</b> " + ContextCompat.getString(context, R.string.already_favorite),
        HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Snackbar.LENGTH_SHORT
    ).setAnchorView(viewGuide)
    mSnackbar.show()
    return mSnackbar
  }

  fun getDateTwoWeeksFromToday(): String {
    val twoWeeksFromNow = LocalDate.now().plusWeeks(2)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return twoWeeksFromNow.format(formatter)
  }
}