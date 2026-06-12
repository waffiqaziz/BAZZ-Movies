package com.waffiq.bazz_movies.core.uihelper.ui.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_watchlist_outlined
import com.waffiq.bazz_movies.core.designsystem.R.string.add_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.add_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.remove_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.remove_from_watchlist

/**
 * Used to differ between Favorite and Watchlist screens:
 * the button labels and the end-side icon.
 */
data class SwipeConfig(
  @StringRes val deleteContentDescription: Int,
  @StringRes val endActionContentDescription: Int,
  @DrawableRes val endActionIcon: Int,
) {
  companion object {
    fun forFavorite() =
      SwipeConfig(
        deleteContentDescription = remove_from_favorite,
        endActionContentDescription = add_to_watchlist,
        endActionIcon = ic_watchlist_outlined,
      )

    fun forWatchlist() =
      SwipeConfig(
        deleteContentDescription = remove_from_watchlist,
        endActionContentDescription = add_to_favorite,
        endActionIcon = ic_hearth,
      )
  }
}
