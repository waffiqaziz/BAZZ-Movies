package com.waffiq.bazz_movies.core.database.data.model.v1

import com.waffiq.bazz_movies.core.database.data.model.GenreV1
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreV1Test {

  @Test
  fun genreV1_getGenreName_returnsCorrectly() {
    assertEquals("Action", GenreV1.ACTION.genreName)
  }
}
