package com.waffiq.bazz_movies.feature.favorite.ui.manager

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SwipeCallbackHelper
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_trash
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bookmark_dark

class SwipeHandler(
  private val context: Context,
  private val onSwipeLeft: (RecyclerView.ViewHolder, Int) -> Unit,
  private val onSwipeRight: (RecyclerView.ViewHolder, Int) -> Unit
) {
  fun setupSwipe(recyclerView: RecyclerView) {
    val swipeCallback = SwipeCallbackHelper(
      onSwipeLeft = { viewHolder, position -> onSwipeLeft(viewHolder, position) },
      onSwipeRight = { viewHolder, position -> onSwipeRight(viewHolder, position) },
      context = context,
      deleteIconResId = ic_trash,
      actionIconResId = ic_bookmark_dark,
    )
    ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
  }
}
