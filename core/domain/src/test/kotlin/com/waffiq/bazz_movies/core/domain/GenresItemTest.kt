package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class GenresItemTest {

  @Test
  fun genresItem_withValidValue_returnsCorrectData() {
    val genre = GenresItem(
      name = "Action",
      id = 28
    )

    assertEquals("Action", genre.name)
    assertEquals(28, genre.id)
  }

  @Test
  fun genresItem_withNullValue_returnsNull() {
    val genre = GenresItem()

    assertNull(genre.name)
    assertNull(genre.id)
  }
}
