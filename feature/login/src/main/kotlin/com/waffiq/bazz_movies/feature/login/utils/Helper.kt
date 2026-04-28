package com.waffiq.bazz_movies.feature.login.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat

object Helper {
  fun Context.loadTypeface(fontRes: Int): Typeface =
    ResourcesCompat.getFont(this, fontRes) ?: Typeface.DEFAULT
}
