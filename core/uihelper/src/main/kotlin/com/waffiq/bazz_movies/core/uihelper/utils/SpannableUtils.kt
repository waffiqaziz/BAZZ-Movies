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
    val text = "$title $text"
    return text.bold(title)
  }

  fun String.bold(target: String): SpannableString {
    val spannable = SpannableString(this)
    val start = indexOf(target)

    if (start >= 0) {
      spannable.setSpan(
        StyleSpan(Typeface.BOLD),
        start,
        start + target.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
    }

    return spannable
  }
}
