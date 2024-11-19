package com.waffiq.bazz_movies.core.movie.utils.helpers

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Used as general helper
 */
object GeneralHelper {
  fun initLinearLayoutManagerHorizontal(context: Context) =
    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

  fun initLinearLayoutManagerVertical(context: Context) =
    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
}
