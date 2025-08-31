package com.waffiq.bazz_movies.feature.favorite.ui.manager

import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.designsystem.R.string.removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.undo
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow

class UndoSnackbar(
  private val context: Context,
  private val anchorViewId: Int
) {
  private var currentSnackbar: Snackbar? = null

  fun show(title: String, isDelete: Boolean, onUndo: () -> Unit) {
    val message = HtmlCompat.fromHtml(
      "<b>$title</b> " + context.getString(
        if (isDelete) removed_from_favorite else added_to_watchlist
      ),
      HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    currentSnackbar = Snackbar.make(
      (context as Activity).findViewById(anchorViewId),
      message,
      Snackbar.LENGTH_LONG
    ).setAction(context.getString(undo)) { onUndo() }
      .setAnchorView(context.findViewById(anchorViewId))
      .setActionTextColor(ContextCompat.getColor(context, yellow))

    currentSnackbar?.show()
  }

  fun dismiss() {
    currentSnackbar?.dismiss()
    currentSnackbar = null
  }
}
