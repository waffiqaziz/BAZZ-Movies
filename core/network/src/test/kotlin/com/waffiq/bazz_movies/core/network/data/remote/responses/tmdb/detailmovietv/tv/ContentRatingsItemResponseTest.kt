package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.contentRatingsItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ContentRatingsItemResponseTest {

  @Test
  fun contentRatingsItemResponse_withValidValues_setsPropertiesCorrectly() {
    val contentRatingsItemResponse = contentRatingsItemResponseDump
    assertEquals("SG", contentRatingsItemResponse.iso31661)
    assertEquals("this is description", contentRatingsItemResponse.descriptors?.get(0))
    assertEquals("PG13", contentRatingsItemResponse.rating)
  }

  @Test
  fun contentRatingsItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val contentRatingsItemResponse = ContentRatingsItemResponse()
    assertNull(contentRatingsItemResponse.iso31661)
    assertNull(contentRatingsItemResponse.rating)
    assertNull(contentRatingsItemResponse.descriptors)
  }

  @Test
  fun contentRatingsItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val contentRatingsItemResponse = ContentRatingsItemResponse(
      iso31661 = "MY",
    )
    assertEquals("MY", contentRatingsItemResponse.iso31661)
    assertNull(contentRatingsItemResponse.rating)
    assertNull(contentRatingsItemResponse.descriptors)
  }
}
