package com.waffiq.bazz_movies.core.model.user

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchlistParamsTest {

  @Test
  fun watchlistParams_withValidValue_returnsCorrectData() {
    val watchlistParams = WatchlistParams(
      mediaType = "movie",
      mediaId = 123,
      watchlist = true,
    )

    assertEquals("movie", watchlistParams.mediaType)
    assertEquals(123, watchlistParams.mediaId)
    assertTrue(watchlistParams.watchlist)
  }
}
