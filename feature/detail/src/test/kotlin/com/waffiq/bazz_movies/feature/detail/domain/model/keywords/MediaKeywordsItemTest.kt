package com.waffiq.bazz_movies.feature.detail.domain.model.keywords

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaKeywordsItemTest {

  @Test
  fun createMediaKeywordsItem_withDefaultValues_shouldCreateInstance() {
    val item = MediaKeywordsItem()
    assertNull(item.name)
    assertNull(item.id)
  }

  @Test
  fun createMediaKeywordsItem_withAllValues_shouldSetPropertiesCorrectly() {
    val item = MediaKeywordsItem(id = 345, name = "family")
    assertEquals(345, item.id)
    assertEquals("family", item.name)
  }
}
