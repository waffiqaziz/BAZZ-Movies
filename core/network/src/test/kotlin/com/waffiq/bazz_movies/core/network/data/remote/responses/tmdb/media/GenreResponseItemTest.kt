package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GenreResponseItemTest {

  @Test
  fun genresResponseItem_withValidValues_setsPropertiesCorrectly() {
    val genresResponseItem = GenresResponseItem(
      name = "Action",
      id = 1,
    )
    assertEquals("Action", genresResponseItem.name)
    assertEquals(1, genresResponseItem.id)
  }

  @Test
  fun genresResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val genresResponseItem = GenresResponseItem()
    assertNull(genresResponseItem.name)
    assertNull(genresResponseItem.id)
  }

  @Test
  fun genresResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val genresResponseItem = GenresResponseItem(
      name = null,
      id = 2,
    )
    assertNull(genresResponseItem.name)
    assertEquals(2, genresResponseItem.id)
  }
}
