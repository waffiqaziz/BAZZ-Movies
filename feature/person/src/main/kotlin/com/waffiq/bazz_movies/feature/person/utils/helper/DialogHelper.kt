package com.waffiq.bazz_movies.feature.person.utils.helper

import android.app.Dialog
import android.graphics.Color
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity.Companion.DIALOG_ALPHA

object DialogHelper {

  fun Dialog.setupTransparentDialog() {
    val window = requireNotNull(window) { "Dialog window must not be null" }
    window.setDimAmount(DIALOG_ALPHA) // set transparent percent
    window.setLayout(
      WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.WRAP_CONTENT,
    )
    window.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
  }
}
