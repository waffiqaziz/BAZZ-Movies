package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.view.Gravity
import android.view.View
import com.google.android.material.listitem.ListItemCardView
import com.google.android.material.listitem.ListItemLayout
import com.google.android.material.listitem.RevealableListItem
import com.google.android.material.listitem.SwipeableListItem

object SwipeCallbackFactory {

  fun createSwipeCallback(
    startLayout: View,
    endLayout: View,
    listItemLayout: ListItemLayout,
    onDelete: () -> Unit,
    onAddToWatchlist: () -> Unit,
  ): ListItemCardView.SwipeCallback =
    object : ListItemCardView.SwipeCallback() {

      override fun onSwipe(swipeDistance: Int) {
        // do nothing
      }

      override fun <V> onSwipeStateChanged(
        newState: Int,
        revealableItem: V,
        swipeDistance: Int,
      ) where V : View?, V : RevealableListItem? {
        if (newState != SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION) return

        when (revealableItem) {
          startLayout -> {
            onDelete()
            listItemLayout.setSwipeState(SwipeableListItem.STATE_CLOSED, Gravity.START)
          }

          endLayout -> {
            onAddToWatchlist()
            listItemLayout.setSwipeState(SwipeableListItem.STATE_CLOSED, Gravity.END)
          }
        }
      }
    }
}
