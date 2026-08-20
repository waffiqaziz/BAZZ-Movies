package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.designsystem.R.string.movie
import com.waffiq.bazz_movies.core.designsystem.R.string.person
import com.waffiq.bazz_movies.core.designsystem.R.string.tv_series
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

class FilterHelperTest {

  @Test
  fun labelRes_mediaTypePerson_returnsPersonString() {
    assertEquals(MediaType.PERSON.labelRes(), person)
  }

  @Test
  fun labelRes_mediaTypeMovie_returnsMovieString() {
    assertEquals(MediaType.MOVIE.labelRes(), movie)
  }

  @Test
  fun labelRes_mediaTypeTV_returnsTVString() {
    assertEquals(MediaType.TV.labelRes(), tv_series)
  }

  @Test
  fun labelRes_mediaTypeMulti_returnsError() {
    assertFailsWith<IllegalStateException> {
      MediaType.MULTI.labelRes()
    }
  }

  @Test
  fun toMediaTypeSet_validMediaTypes_returnsCorrectly() {
    val result = listOf("PERSON", "MOVIE").toMediaTypeSet()

    assertEquals(setOf(MediaType.PERSON, MediaType.MOVIE), result)
  }

  @Test
  fun toMediaTypeSet_invalidMediaTypes_shouldIgnore() {
    val result = listOf("PERSON", "INVALID").toMediaTypeSet()

    assertEquals(setOf(MediaType.PERSON), result)
  }

  @Test
  fun toMediaTypeSet_listIsNull_returnsMULTI() {
    val result = null.toMediaTypeSet()

    assertEquals(setOf(MediaType.MULTI), result)
  }

  @Test
  fun toMediaTypeSet_listIsEmpty_returnsMULTI() {
    val result = emptyList<String>().toMediaTypeSet()

    assertEquals(setOf(MediaType.MULTI), result)
  }
}
