package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class FavoriteModelTest {

  @Test
  fun favoriteModel_withValidValue_returnsCorrectData() {
    val model = FavoriteModel(
      mediaType = "movie",
      mediaId = 100,
      favorite = true
    )

    assertEquals("movie", model.mediaType)
    assertEquals(100, model.mediaId)
    assertTrue(model.favorite)
  }
}
