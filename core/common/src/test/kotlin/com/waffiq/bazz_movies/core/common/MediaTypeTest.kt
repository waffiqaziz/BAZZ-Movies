package com.waffiq.bazz_movies.core.common

import com.waffiq.bazz_movies.core.common.MediaType.Companion.fromValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaTypeTest {

  @Test
  fun fromValue_withValidValue_returnsCorrectly() {
    assertEquals(fromValue("person"), MediaType.PERSON)
    assertNull(fromValue(null))
    assertNull(fromValue("unknown"))
  }

  @Test
  fun mediaType_withCorrectValue_returnsLowerCase() {
    assertEquals("movie", MediaType.MOVIE.value)
  }
}
