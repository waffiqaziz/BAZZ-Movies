package com.waffiq.bazz_movies.core.network.data.remote.models

import org.junit.Assert.assertEquals
import org.junit.Test

class RatePostModelTest {

  @Test
  fun userModel_withValidValues_setsPropertiesCorrectly() {
    val ratePostModel = RatePostModel(value = 10.0f)
    assertEquals(10.0f, ratePostModel.value)
  }
}
