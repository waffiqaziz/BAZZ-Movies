package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieKeywordsResponseTest {
  @Test
  fun movieKeywordsResponse_withValidValues_setsPropertiesCorrectly() {
    val movieKeywordsResponse = MovieKeywordsResponse(
      keywords = listOf(MediaKeywordsResponseItem(id = 111, name = "war")),
    )
    assertEquals(111, movieKeywordsResponse.keywords?.get(0)?.id)
  }

  @Test
  fun movieKeywordsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val movieKeywordsResponse = MovieKeywordsResponse()
    assertNull(movieKeywordsResponse.keywords)
  }
}
