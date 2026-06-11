package com.waffiq.bazz_movies.core.utils.testutils

import com.waffiq.bazz_movies.core.utils.RatingHelper.ratingHandler
import com.waffiq.bazz_movies.core.utils.RatingHelper.setRatingBar
import org.junit.Assert.assertEquals
import org.junit.Test

class RatingHelperTest {

  @Test
  fun ratingHandler_correctValue_returnsCorrectly() {
    assertEquals("5/10", ratingHandler(5f))
    assertEquals("0/10", ratingHandler(null))
  }

  @Test
  fun setRatingBar_correctValue_returnsCorrectly() {
    assertEquals(2.5f, setRatingBar(5f))
    assertEquals(0.0f, setRatingBar(null))
  }
}
