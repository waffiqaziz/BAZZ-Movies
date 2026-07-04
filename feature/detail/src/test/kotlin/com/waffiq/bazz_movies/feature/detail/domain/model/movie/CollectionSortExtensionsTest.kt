package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.partsItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionSortExtensionsTest {

  private val data = listOf(partsItem)

  @Test
  fun sortedByOption_allPosibility_shouldSortingCorrectly() {
    assertEquals(data, data.sortedByOption(CollectionSortOption.TITLE_AZ))
    assertEquals(data, data.sortedByOption(CollectionSortOption.TITLE_ZA))
    assertEquals(data, data.sortedByOption(CollectionSortOption.RATING_HIGH_TO_LOW))
    assertEquals(data, data.sortedByOption(CollectionSortOption.RATING_LOW_TO_HIGH))
    assertEquals(data, data.sortedByOption(CollectionSortOption.POPULARITY_HIGH_TO_LOW))
    assertEquals(data, data.sortedByOption(CollectionSortOption.POPULARITY_LOW_TO_HIGH))
    assertEquals(data, data.sortedByOption(CollectionSortOption.RELEASE_DATE_NEWEST))
    assertEquals(data, data.sortedByOption(CollectionSortOption.RELEASE_DATE_OLDEST))
  }
}
