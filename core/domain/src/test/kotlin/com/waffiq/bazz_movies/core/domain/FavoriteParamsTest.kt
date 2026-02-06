package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class FavoriteParamsTest {

  @Test
  fun favoriteParams_withValidValue_returnsCorrectData() {
    val favoriteParams = FavoriteParams(
      mediaType = "movie",
      mediaId = 100,
      favorite = true
    )

    assertEquals("movie", favoriteParams.mediaType)
    assertEquals(100, favoriteParams.mediaId)
    assertTrue(favoriteParams.favorite)
  }
}
