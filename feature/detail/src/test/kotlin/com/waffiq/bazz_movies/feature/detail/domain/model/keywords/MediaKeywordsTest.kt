package com.waffiq.bazz_movies.feature.detail.domain.model.keywords

import org.junit.Assert.assertNull
import org.junit.Test

class MediaKeywordsTest {

  @Test
  fun createMediaKeywords_withDefaultValues_shouldCreateInstance() {
    val item = MediaKeywords()
    assertNull(item.id)
    assertNull(item.keywords)
  }
}
