package com.waffiq.bazz_movies.navigation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ListTypeTest {

  @Test
  fun shouldUpdateBackdrop_whenByGenre_expectedFalse() {
    val result = ListType.BY_GENRE.shouldUpdateBackdrop()

    assertFalse(result)
  }

  @Test
  fun shouldUpdateBackdrop_whenRecommendation_expectedFalse() {
    val result = ListType.RECOMMENDATION.shouldUpdateBackdrop()

    assertFalse(result)
  }

  @Test
  fun shouldUpdateBackdrop_whenOtherType_expectedTrue() {
    val result = ListType.POPULAR.shouldUpdateBackdrop()

    assertTrue(result)
  }
}
