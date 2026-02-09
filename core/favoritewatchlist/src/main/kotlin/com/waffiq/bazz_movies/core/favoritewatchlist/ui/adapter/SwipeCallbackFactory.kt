package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import com.google.android.material.listitem.ListItemCardView
import com.google.android.material.listitem.ListItemLayout
import com.google.android.material.listitem.RevealableListItem
import com.google.android.material.listitem.SwipeableListItem

object SwipeCallbackFactory {

  fun <T> createSwipeCallback(
    startLayoutProvider: () -> View?,
    endLayoutProvider: () -> View?,
    listItemLayoutProvider: () -> ListItemLayout,
    dataProvider: () -> T,
    positionProvider: () -> Int,
    onDelete: (T, Int) -> Unit,
    onAddToWatchlist: (T, Int) -> Unit,
  ): ListItemCardView.SwipeCallback =
    object : ListItemCardView.SwipeCallback() {

      override fun onSwipe(swipeDistance: Int) {
        // Called continuously as user swipes
      }

      override fun <V> onSwipeStateChanged(
        newState: Int,
        revealableItem: V,
        swipeDistance: Int,
      ) where V : View?, V : RevealableListItem? {
        @SuppressLint("SwitchIntDef")
        when (newState) {
          SwipeableListItem.STATE_SWIPE_PRIMARY_ACTION -> {
            when (revealableItem) {
              startLayoutProvider() -> {
                // Swiped RIGHT - Delete action
                onDelete(dataProvider(), positionProvider())
                listItemLayoutProvider().setSwipeState(
                  SwipeableListItem.STATE_CLOSED,
                  Gravity.START,
                )
              }

              endLayoutProvider() -> {
                // Swiped LEFT - Add to Watchlist action
                onAddToWatchlist(dataProvider(), positionProvider())
                listItemLayoutProvider().setSwipeState(
                  SwipeableListItem.STATE_CLOSED,
                  Gravity.END,
                )
              }
            }
          }
        }
      }
    }
}
