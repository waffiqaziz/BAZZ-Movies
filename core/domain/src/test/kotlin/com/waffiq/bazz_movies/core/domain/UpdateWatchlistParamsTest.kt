package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UpdateWatchlistParamsTest {

  @Test
  fun updateWatchlistParams_withValidValue_returnsCorrectData() {
    val watchlistParams = UpdateWatchlistParams(
      mediaType = "movie",
      mediaId = 123,
      watchlist = true
    )

    assertEquals("movie", watchlistParams.mediaType)
    assertEquals(123, watchlistParams.mediaId)
    assertTrue(watchlistParams.watchlist)
  }
}
