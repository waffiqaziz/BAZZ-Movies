package com.waffiq.bazz_movies.core.network.data.remote.responses.omdb

import org.junit.Assert.assertNull
import org.junit.Test

class RatingsResponseItemTest {

  @Test
  fun ratingsResponseItem_nullValues_setsNullValue() {
    val ratingsResponseItem = RatingsResponseItem()
    assertNull(ratingsResponseItem.value)
    assertNull(ratingsResponseItem.source)
  }
}
