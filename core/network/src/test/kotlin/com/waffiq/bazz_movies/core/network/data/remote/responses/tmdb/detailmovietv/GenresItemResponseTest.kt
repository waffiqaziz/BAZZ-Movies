package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class GenresItemResponseTest {

  @Test
  fun genresItemResponse_withValidValues_setsPropertiesCorrectly() {
    val genresItemResponse = GenresItemResponse(
      name = "Action",
      id = 1
    )
    assertEquals("Action", genresItemResponse.name)
    assertEquals(1, genresItemResponse.id)
  }

  @Test
  fun genresItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val genresItemResponse = GenresItemResponse()
    assertNull(genresItemResponse.name)
    assertNull(genresItemResponse.id)
  }

  @Test
  fun genresItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val genresItemResponse = GenresItemResponse(
      name = null,
      id = 2
    )
    assertNull(genresItemResponse.name)
    assertEquals(2, genresItemResponse.id)
  }
}
