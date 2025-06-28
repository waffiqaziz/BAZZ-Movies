package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class StatedTest {

  @Test
  fun stated_withValidValue_returnsCorrectData() {
    val stated = Stated(
      id = 101,
      favorite = true,
      rated = Rated.Value(7.5),
      watchlist = false
    )

    assertEquals(101, stated.id)
    assertTrue(stated.favorite)
    assertTrue(stated.rated is Rated.Value)
    assertEquals(7.5, (stated.rated as Rated.Value).value, 0.0)
    assertFalse(stated.watchlist)
  }

  @Test
  fun stated_withUnratedValue_returnsCorrectData() {
    val stated = Stated(
      id = 102,
      favorite = false,
      rated = Rated.Unrated,
      watchlist = true
    )

    assertEquals(102, stated.id)
    assertFalse(stated.favorite)
    assertEquals(Rated.Unrated, stated.rated)
    assertTrue(stated.watchlist)
  }
}
