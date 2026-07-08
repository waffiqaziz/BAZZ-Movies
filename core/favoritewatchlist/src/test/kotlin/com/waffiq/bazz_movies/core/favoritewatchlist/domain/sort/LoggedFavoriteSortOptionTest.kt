package com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort

import com.waffiq.bazz_movies.core.designsystem.R.string.recently_added
import org.junit.Assert.assertEquals
import org.junit.Test

class LoggedFavoriteSortOptionTest {

  @Test
  fun getLabel_withCorrectEnum_returnsCorrectLabel() {
    assertEquals(LoggedFavoriteSortOption.RECENTLY_ADDED.label, recently_added)
  }
}
