package com.waffiq.bazz_movies.core.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Used as general helper
 */
object GeneralHelper {

  fun initLinearLayoutManagerVertical(context: Context) =
    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
}
