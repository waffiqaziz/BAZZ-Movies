package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TvKeywordsResponseTest {
  @Test
  fun tvKeywordsResponse_withValidValues_setsPropertiesCorrectly() {
    val tvKeywordsResponse = TvKeywordsResponse(
      id = 1222,
      keywords = listOf(
        MediaKeywordsResponseItem(id = 456456, name = "war")
      )
    )
    assertEquals(1222, tvKeywordsResponse.id)
    assertEquals(456456, tvKeywordsResponse.keywords?.get(0)?.id)
  }

  @Test
  fun tvKeywordsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val tvKeywordsResponse = TvKeywordsResponse()
    assertNull(tvKeywordsResponse.id)
    assertNull(tvKeywordsResponse.keywords)
  }

  @Test
  fun tvKeywordsResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val tvKeywordsResponse = TvKeywordsResponse(id = 678678)
    assertEquals(678678, tvKeywordsResponse.id)
    assertNull(tvKeywordsResponse.keywords)
  }
}
