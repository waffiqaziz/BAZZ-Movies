package com.waffiq.bazz_movies.feature.list.utils

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewLayoutHelper {

  val RecyclerView.saveInstanceState get() =
    requireNotNull(layoutManager?.onSaveInstanceState()) { "layout manager must not be null" }

  fun RecyclerView.restoreInstanceState(savedState: Parcelable) {
    layoutManager?.onRestoreInstanceState(savedState)
  }
}
