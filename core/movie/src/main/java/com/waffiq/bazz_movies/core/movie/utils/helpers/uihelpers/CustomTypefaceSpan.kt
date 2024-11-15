package com.waffiq.bazz_movies.core.movie.utils.helpers.uihelpers

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.StyleSpan

/**
 * Used to change  font family of error text in EditText.
 */
class CustomTypefaceSpan(private val typeface: Typeface) : StyleSpan(Typeface.NORMAL) {
  override fun updateDrawState(ds: TextPaint) {
    super.updateDrawState(ds)
    ds.typeface = typeface
  }

  override fun updateMeasureState(paint: TextPaint) {
    super.updateMeasureState(paint)
    paint.typeface = typeface
  }
}
