package com.waffiq.bazz_movies.core.common

import com.waffiq.bazz_movies.core.common.Genre.Companion.forMediaType
import com.waffiq.bazz_movies.core.common.Genre.Companion.fromId
import com.waffiq.bazz_movies.core.common.Genre.Companion.fromName
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GenreTest {

  @Test
  fun getValue_fromValidGenre_returnsCorrectly() {
    assertEquals("Comedy", Genre.COMEDY.genreName)
    assertEquals(35, Genre.COMEDY.id)
  }

  @Test
  fun fromId_withValidValue_returnsCorrectly() {
    assertEquals(Genre.WAR_AND_POLITICS, fromId(10768))
    assertNull(fromId(111111))
  }

  @Test
  fun fromName_withValidValue_returnsCorrectly() {
    assertEquals(Genre.WESTERN, fromName("Western"))
    assertNull(fromName("unknown"))
  }

  @Test
  fun forMediaType_withValidValue_returnsCorrectly() {
    assertNotNull(forMediaType(MediaType.MOVIE))
    assertNotNull(forMediaType(MediaType.TV))
  }
}
