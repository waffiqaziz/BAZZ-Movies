package com.waffiq.bazz_movies.core.models

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class FavoriteTest {
  private val favoriteValid = Favorite(
    id = 1,
    mediaId = 100,
    mediaType = "movie",
    genre = "Action",
    backDrop = "backdrop.jpg",
    poster = "poster.jpg",
    overview = "Some overview",
    title = "Sample Movie",
    releaseDate = "2023-01-01",
    popularity = 9.0,
    rating = 4.5f,
    isFavorite = true,
    isWatchlist = false,
    lastUpdated = 4343,
  )

  @Test
  fun favorite_withValidValue_returnsCorrectData() {
    assertEquals(1, favoriteValid.id)
    assertEquals(100, favoriteValid.mediaId)
    assertEquals("movie", favoriteValid.mediaType)
    assertEquals("Action", favoriteValid.genre)
    assertEquals("backdrop.jpg", favoriteValid.backDrop)
    assertEquals("poster.jpg", favoriteValid.poster)
    assertEquals("Some overview", favoriteValid.overview)
    assertEquals("Sample Movie", favoriteValid.title)
    assertEquals("2023-01-01", favoriteValid.releaseDate)
    assertEquals(9.0, favoriteValid.popularity, 0.0)
    assertEquals(4.5f, favoriteValid.rating)
    assertEquals(4343, favoriteValid.lastUpdated)
    assertTrue(favoriteValid.isFavorite)
    assertFalse(favoriteValid.isWatchlist)
  }

  @Test
  fun isStalled_withCorrectValue_returnsCorrectly() {
    assertTrue(favoriteValid.isStale())
    assertFalse(favoriteValid.copy(lastUpdated = System.currentTimeMillis()).isStale())
  }
}
