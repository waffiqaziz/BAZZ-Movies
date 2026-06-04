package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.FavWatchlistHelper.ratingHandler
import org.junit.Assert.assertEquals
import org.junit.Test

class FavWatchlistHelperTest {

  @Test
  fun ratingHandler_mixedValue_returnsCorrectly() {
    assertEquals("5/10", ratingHandler(5f))
    assertEquals("0/10", ratingHandler(null))
  }
}
