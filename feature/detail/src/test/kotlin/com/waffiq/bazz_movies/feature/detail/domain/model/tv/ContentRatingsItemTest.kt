package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentRatingsItemTest {

  @Test
  fun createContentRatingsItem_withValidValues_setsPropertiesCorrectly() {
    val descriptors = listOf("Violence", "Language", "Adult Content")
    val contentRating = ContentRatingsItem(
      descriptors = descriptors,
      iso31661 = "US",
      rating = "TV-MA"
    )

    assertEquals(descriptors, contentRating.descriptors)
    assertEquals("US", contentRating.iso31661)
    assertEquals("TV-MA", contentRating.rating)
  }

  @Test
  fun createContentRatingsItem_withDefaultValues_setsAllPropertiesToNull() {
    val contentRating = ContentRatingsItem()

    assertNull(contentRating.descriptors)
    assertNull(contentRating.iso31661)
    assertNull(contentRating.rating)
  }

  @Test
  fun createContentRatingsItem_withPartialValues_setsSpecifiedPropertiesOnly() {
    val contentRating = ContentRatingsItem(
      iso31661 = "GB",
      rating = "15"
    )

    assertNull(contentRating.descriptors)
    assertEquals("GB", contentRating.iso31661)
    assertEquals("15", contentRating.rating)
  }

  @Test
  fun createContentRatingsItem_withEmptyDescriptors_setsEmptyList() {
    val contentRating = ContentRatingsItem(
      descriptors = emptyList()
    )

    assertTrue(contentRating.descriptors?.isEmpty() == true)
  }

  @Test
  fun createContentRatingsItem_withNullDescriptors_setsNullDescriptors() {
    val descriptors = listOf(null, "Violence", null)
    val contentRating = ContentRatingsItem(
      descriptors = descriptors
    )

    assertEquals(descriptors, contentRating.descriptors)
    assertEquals(3, contentRating.descriptors?.size)
    assertNull(contentRating.descriptors?.get(0))
    assertEquals("Violence", contentRating.descriptors?.get(1))
    assertNull(contentRating.descriptors?.get(2))
  }

  @Test
  fun createContentRatingsItem_withEmptyStrings_setsEmptyStrings() {
    val contentRating = ContentRatingsItem(
      iso31661 = "",
      rating = ""
    )

    assertEquals("", contentRating.iso31661)
    assertEquals("", contentRating.rating)
  }

  @Test
  fun createContentRatingsItem_withMixedDescriptors_setsMixedDescriptors() {
    val descriptors = listOf("Violence", 123, true, null, "Language")
    val contentRating = ContentRatingsItem(
      descriptors = descriptors
    )

    assertEquals(descriptors, contentRating.descriptors)
    assertEquals(5, contentRating.descriptors?.size)
    assertEquals("Violence", contentRating.descriptors?.get(0))
    assertEquals(123, contentRating.descriptors?.get(1))
    assertEquals(true, contentRating.descriptors?.get(2))
    assertNull(contentRating.descriptors?.get(3))
    assertEquals("Language", contentRating.descriptors?.get(4))
  }
}
