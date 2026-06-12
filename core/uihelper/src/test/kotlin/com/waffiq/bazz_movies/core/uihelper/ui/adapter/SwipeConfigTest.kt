package com.waffiq.bazz_movies.core.uihelper.ui.adapter

import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_watchlist_outlined
import com.waffiq.bazz_movies.core.designsystem.R.string.add_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.add_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.remove_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.remove_from_watchlist
import org.junit.Assert.assertEquals
import org.junit.Test

class SwipeConfigTest {

  @Test
  fun forFavorite_whenUseThisCofig_shouldReturnWathclistIcon() {
    val result = SwipeConfig.forFavorite()
    assertEquals(ic_watchlist_outlined, result.endActionIcon)
    assertEquals(add_to_watchlist, result.endActionContentDescription)
    assertEquals(remove_from_favorite, result.deleteContentDescription)
  }

  @Test
  fun forWatchlist_whenUseThisCofig_shouldReturnHearthIcon() {
    val result = SwipeConfig.forWatchlist()
    assertEquals(ic_hearth, result.endActionIcon)
    assertEquals(add_to_favorite, result.endActionContentDescription)
    assertEquals(remove_from_watchlist, result.deleteContentDescription)
  }
}
