package com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort

import com.waffiq.bazz_movies.core.designsystem.R.string.title_az
import org.junit.Assert.assertEquals
import org.junit.Test

class GuestFavoriteSortOptionTest {

  @Test
  fun getLabel_withCorrectEnum_returnsCorrectLabel() {
    assertEquals(GuestFavoriteSortOption.TITLE_AZ.label, title_az)
  }
}
