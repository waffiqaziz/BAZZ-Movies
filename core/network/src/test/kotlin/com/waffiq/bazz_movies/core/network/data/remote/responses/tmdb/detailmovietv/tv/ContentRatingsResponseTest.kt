package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.contentRatingsResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ContentRatingsResponseTest {

  @Test
  fun contentRatingsResponse_withValidValues_setsPropertiesCorrectly() {
    val contentRatingsResponse = contentRatingsResponseDump
    assertEquals("PG13", contentRatingsResponse.contentRatingsItemResponse?.get(0)?.rating)
  }

  @Test
  fun contentRatingsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val contentRatingsResponse = ContentRatingsResponse()
    assertNull(contentRatingsResponse.contentRatingsItemResponse)
  }
}
