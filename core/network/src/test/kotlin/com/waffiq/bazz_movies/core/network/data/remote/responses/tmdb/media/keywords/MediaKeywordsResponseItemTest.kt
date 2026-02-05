package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaKeywordsResponseItemTest {
  @Test
  fun mediaKeywordsResponseItem_withValidValues_setsPropertiesCorrectly() {
    val mediaKeywordsResponseItem = MediaKeywordsResponseItem(
      id = 4444,
      name = "family"
    )
    assertEquals(4444, mediaKeywordsResponseItem.id)
    assertEquals("family", mediaKeywordsResponseItem.name)
  }

  @Test
  fun mediaKeywordsResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val mediaKeywordsResponseItem = MediaKeywordsResponseItem()
    assertNull(mediaKeywordsResponseItem.id)
    assertNull(mediaKeywordsResponseItem.name)
  }
}
