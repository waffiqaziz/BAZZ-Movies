package com.waffiq.bazz_movies.core.uihelper.utils

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan

object SpannableUtils {

  fun buildActionMessage(
    title: String,
    text: String,
  ): SpannableString {
    return SpannableString("$title $text").apply {
      setSpan(
        StyleSpan(Typeface.BOLD),
        0,
        title.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
    }
  }
}
