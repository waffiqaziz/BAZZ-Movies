package com.waffiq.bazz_movies.core.favoritewatchlist.testutils

import androidx.recyclerview.widget.RecyclerView

object UtilHelper {

  /**
   * Returns a ViewHolder at [position] from a laid-out RecyclerView.
   */
  inline fun <reified VH : RecyclerView.ViewHolder> getViewHolderAt(
    recyclerView: RecyclerView,
    position: Int,
  ): VH =
    recyclerView.findViewHolderForAdapterPosition(position) as? VH
      ?: error(
        "No ViewHolder found at position $position. " +
          "Make sure the RecyclerView is laid out and the position is visible.",
      )
}
