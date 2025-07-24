package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentRatingsTest {

  @Test
  fun createContentRatings_withValidValues_setsPropertiesCorrectly() {
    val ratingsItem = ContentRatingsItem(
      descriptors = listOf("Violence"),
      iso31661 = "US",
      rating = "TV-MA"
    )
    val contentRatings = ContentRatings(
      contentRatingsItem = listOf(ratingsItem)
    )

    assertEquals(1, contentRatings.contentRatingsItem?.size)
    assertEquals(ratingsItem, contentRatings.contentRatingsItem?.get(0))
  }

  @Test
  fun createContentRatings_withDefaultValues_setsAllPropertiesToNull() {
    val contentRatings = ContentRatings()

    assertNull(contentRatings.contentRatingsItem)
  }

  @Test
  fun createContentRatings_withEmptyList_setsEmptyList() {
    val contentRatings = ContentRatings(
      contentRatingsItem = emptyList()
    )

    assertTrue(contentRatings.contentRatingsItem?.isEmpty() == true)
  }

  @Test
  fun createContentRatings_withNullItems_setsNullItems() {
    val contentRatings = ContentRatings(
      contentRatingsItem = listOf(null, null)
    )

    assertEquals(2, contentRatings.contentRatingsItem?.size)
    assertNull(contentRatings.contentRatingsItem?.get(0))
    assertNull(contentRatings.contentRatingsItem?.get(1))
  }

  @Test
  fun createContentRatings_withMixedItems_setsMixedItems() {
    val ratingsItem = ContentRatingsItem(rating = "PG-13")
    val contentRatings = ContentRatings(
      contentRatingsItem = listOf(ratingsItem, null, ratingsItem)
    )

    assertEquals(3, contentRatings.contentRatingsItem?.size)
    assertEquals(ratingsItem, contentRatings.contentRatingsItem?.get(0))
    assertNull(contentRatings.contentRatingsItem?.get(1))
    assertEquals(ratingsItem, contentRatings.contentRatingsItem?.get(2))
  }

  @Test
  fun createContentRatings_withMultipleItems_setsMultipleItems() {
    val usRating = ContentRatingsItem(iso31661 = "US", rating = "TV-MA")
    val gbRating = ContentRatingsItem(iso31661 = "GB", rating = "15")
    val contentRatings = ContentRatings(
      contentRatingsItem = listOf(usRating, gbRating)
    )

    assertEquals(2, contentRatings.contentRatingsItem?.size)
    assertEquals(usRating, contentRatings.contentRatingsItem?.get(0))
    assertEquals(gbRating, contentRatings.contentRatingsItem?.get(1))
  }
}
