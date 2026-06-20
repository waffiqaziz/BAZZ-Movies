package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TvKeywordsResponseTest {

  @Test
  fun tvKeywordsResponse_withValidValues_setsPropertiesCorrectly() {
    val tvKeywordsResponse = TvKeywordsResponse(
      keywords = listOf(MediaKeywordsResponseItem(id = 456456, name = "war")),
    )
    assertEquals(456456, tvKeywordsResponse.keywords?.get(0)?.id)
  }

  @Test
  fun tvKeywordsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val tvKeywordsResponse = TvKeywordsResponse()
    assertNull(tvKeywordsResponse.keywords)
  }
}
