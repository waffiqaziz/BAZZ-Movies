package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieKeywordsResponseTest {
  @Test
  fun movieKeywordsResponse_withValidValues_setsPropertiesCorrectly() {
    val movieKeywordsResponse = MovieKeywordsResponse(
      id = 12345,
      keywords = listOf(
        MediaKeywordsResponseItem(id = 111, name = "war")
      )
    )
    assertEquals(12345, movieKeywordsResponse.id)
    assertEquals(111, movieKeywordsResponse.keywords?.get(0)?.id)
  }

  @Test
  fun movieKeywordsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val movieKeywordsResponse = MovieKeywordsResponse()
    assertNull(movieKeywordsResponse.id)
    assertNull(movieKeywordsResponse.keywords)
  }

  @Test
  fun movieKeywordsResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val movieKeywordsResponse = MovieKeywordsResponse(id = 123123)
    assertEquals(123123, movieKeywordsResponse.id)
    assertNull(movieKeywordsResponse.keywords)
  }
}
