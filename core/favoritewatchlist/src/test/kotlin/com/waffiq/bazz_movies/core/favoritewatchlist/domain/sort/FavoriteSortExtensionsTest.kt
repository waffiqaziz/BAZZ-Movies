package com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort

import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favorite
import com.waffiq.bazz_movies.core.network.data.remote.constants.SortBy.CREATED_AT_ASC
import com.waffiq.bazz_movies.core.network.data.remote.constants.SortBy.CREATED_AT_DESC
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteSortExtensionsTest {

  private val data = listOf(favorite)

  @Test
  fun sortedByOption_allPosibility_shouldSortingCorrectly() {
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.RECENTLY_ADDED))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.OLDEST_ADDED))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.TITLE_AZ))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.TITLE_ZA))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.RATING_HIGH_TO_LOW))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.RATING_LOW_TO_HIGH))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.POPULARITY_HIGH_TO_LOW))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.POPULARITY_LOW_TO_HIGH))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.RELEASE_DATE_NEWEST))
    assertEquals(data, data.sortedByOption(GuestFavoriteSortOption.RELEASE_DATE_OLDEST))
  }

  @Test
  fun toQueryString_validSort_shouldTransformToQueryCorrectly() {
    assertEquals(LoggedFavoriteSortOption.RECENTLY_ADDED.toQueryString(), CREATED_AT_DESC)
    assertEquals(LoggedFavoriteSortOption.OLDEST_ADDED.toQueryString(), CREATED_AT_ASC)
  }
}
