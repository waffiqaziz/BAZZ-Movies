package com.waffiq.bazz_movies.core.model.media

import com.waffiq.bazz_movies.core.model.media.Genre.Companion.forMediaType
import com.waffiq.bazz_movies.core.model.media.Genre.Companion.fromId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GenreTest {

  @Test
  fun getValue_fromValidGenre_returnsCorrectly() {
    assertEquals(35, Genre.COMEDY.id)
    assertEquals(Genre.WAR_AND_POLITICS, fromId(10768))
    assertNull(fromId(111111))
  }

  @Test
  fun forMediaType_withValidValue_returnsCorrectly() {
    assertNotNull(forMediaType(MediaType.MOVIE))
    assertNotNull(forMediaType(MediaType.TV))
  }
}
