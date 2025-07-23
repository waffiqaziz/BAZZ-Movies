package com.waffiq.bazz_movies.feature.detail.domain.model.omdb

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RatingsItemTest {

  @Test
  fun createRatingsItem_withDefaultValues_shouldCreateInstance() {
    val item = RatingsItem()

    assertNull(item.value)
    assertNull(item.source)
  }

  @Test
  fun createRatingsItem_withAllValues_shouldSetPropertiesCorrectly() {
    val value = "8.5"
    val source = "IMDb"

    val item = RatingsItem(
      value = value,
      source = source
    )

    assertEquals(value, item.value)
    assertEquals(source, item.source)
  }
}
