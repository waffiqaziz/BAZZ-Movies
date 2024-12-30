package com.waffiq.bazz_movies.core.uihelper.utils

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class CustomSnapHelper(private val offsetPx: Int = DEFAULT_OFFSET) : LinearSnapHelper() {

  companion object {
    private const val DEFAULT_OFFSET = -50
  }

  override fun calculateDistanceToFinalSnap(
    layoutManager: RecyclerView.LayoutManager,
    targetView: View
  ): IntArray? {
    val distances = super.calculateDistanceToFinalSnap(layoutManager, targetView)

    distances?.let {
      if (layoutManager.canScrollHorizontally()) {
        it[0] -= offsetPx
      }
    }
    return distances
  }
}
