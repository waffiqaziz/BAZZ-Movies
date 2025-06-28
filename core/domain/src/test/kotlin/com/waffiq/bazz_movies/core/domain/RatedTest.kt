package com.waffiq.bazz_movies.core.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class RatedTest {

  @Test
  fun ratedValue_withValidValue_returnsCorrectData() {
    val rated = Rated.Value(8.5)
    assertEquals(8.5, rated.value, 0.0)
  }

  @Test
  fun ratedUnrated_withValidValue_returnsCorrectData() {
    val rated = Rated.Unrated
    assertEquals(Rated.Unrated, rated)
  }
}
