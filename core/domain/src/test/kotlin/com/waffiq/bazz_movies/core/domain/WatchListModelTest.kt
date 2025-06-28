package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class WatchListModelTest {

  @Test
  fun watchlistModel_withValidValue_returnsCorrectData() {
    val model = WatchlistModel(
      mediaType = "movie",
      mediaId = 123,
      watchlist = true
    )

    assertEquals("movie", model.mediaType)
    assertEquals(123, model.mediaId)
    assertTrue(model.watchlist)
  }
}
