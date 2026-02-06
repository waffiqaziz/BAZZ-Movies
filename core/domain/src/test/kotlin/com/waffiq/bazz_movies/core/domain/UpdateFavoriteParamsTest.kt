package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UpdateFavoriteParamsTest {

  @Test
  fun updateFavoriteParams_withValidValue_returnsCorrectData() {
    val favoriteParams = UpdateFavoriteParams(
      mediaType = "movie",
      mediaId = 100,
      favorite = true
    )

    assertEquals("movie", favoriteParams.mediaType)
    assertEquals(100, favoriteParams.mediaId)
    assertTrue(favoriteParams.favorite)
  }
}
