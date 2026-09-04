package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.contentRatingsResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ContentRatingsResponseItemTest {

  @Test
  fun contentRatingsResponseItem_withValidValues_setsPropertiesCorrectly() {
    val contentRatingsResponseItem = contentRatingsResponseItem
    assertEquals("SG", contentRatingsResponseItem.iso31661)
    assertEquals("this is description", contentRatingsResponseItem.descriptors?.get(0))
    assertEquals("PG13", contentRatingsResponseItem.rating)
  }

  @Test
  fun contentRatingsResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val contentRatingsResponseItem = ContentRatingsResponseItem()
    assertNull(contentRatingsResponseItem.iso31661)
    assertNull(contentRatingsResponseItem.rating)
    assertNull(contentRatingsResponseItem.descriptors)
  }

  @Test
  fun contentRatingsResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val contentRatingsResponseItem = ContentRatingsResponseItem(
      iso31661 = "MY",
    )
    assertEquals("MY", contentRatingsResponseItem.iso31661)
    assertNull(contentRatingsResponseItem.rating)
    assertNull(contentRatingsResponseItem.descriptors)
  }
}
