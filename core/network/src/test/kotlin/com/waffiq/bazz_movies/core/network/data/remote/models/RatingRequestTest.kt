package com.waffiq.bazz_movies.core.network.data.remote.models

import org.junit.Assert.assertEquals
import org.junit.Test

class RatingRequestTest {

  @Test
  fun userModel_withValidValues_setsPropertiesCorrectly() {
    val ratingRequest = RatingRequest(value = 10.0f)
    assertEquals(10.0f, ratingRequest.value)
  }
}
