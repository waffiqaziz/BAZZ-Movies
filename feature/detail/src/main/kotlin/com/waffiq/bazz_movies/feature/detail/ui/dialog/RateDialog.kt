package com.waffiq.bazz_movies.feature.detail.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import androidx.core.graphics.drawable.toDrawable
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.feature.detail.R.id.btn_cancel
import com.waffiq.bazz_movies.feature.detail.R.id.btn_submit
import com.waffiq.bazz_movies.feature.detail.R.id.rating_bar_action
import com.waffiq.bazz_movies.feature.detail.R.layout.dialog_rating

/**
 * A dialog for rating a movie or TV show.

 * This dialog allows users to submit a rating for movie or tv-series.
 *
 * @param context The context in which the dialog is displayed.
 * @param currentRating The current rating of the media item, represented as a string.
 * @param onSubmit Callback function to handle the submission of the rating.
 */
class RateDialog(
  private val context: Context,
  private val currentRating: String,
  private val onSubmit: (Float) -> Unit,
) {

  fun show() {
    val dialog = Dialog(context)
    val dialogView = View.inflate(context, dialog_rating, null)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(dialogView)
    dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

    val ratingBar = dialogView.findViewById<RatingBar>(rating_bar_action)
    ratingBar.rating =
      if (currentRating == context.getString(not_available)) {
        0.0f
      } else {
        currentRating.toFloat() / 2
      }

    val btnSubmit: Button = dialogView.findViewById(btn_submit)
    val btnCancel: Button = dialogView.findViewById(btn_cancel)

    btnSubmit.setOnClickListener {
      val rating = ratingBar.rating * 2
      onSubmit(rating)
      dialog.dismiss()
    }

    btnCancel.setOnClickListener { dialog.dismiss() }
    dialog.show()
  }
}
