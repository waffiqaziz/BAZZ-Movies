package com.waffiq.bazz_movies.feature.person.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class CastItemTest {

  @Test
  fun castItem_withNullValue_returnsDefaultValue() {
    val castItem = CastItem()

    assertNull(castItem.firstAirDate)
    assertNull(castItem.overview)
    assertNull(castItem.originalLanguage)
    assertEquals(castItem.episodeCount, 0)
    assertNull(castItem.listGenreIds)
    assertNull(castItem.posterPath)
    assertNull(castItem.listOriginCountry)
    assertNull(castItem.backdropPath)
    assertNull(castItem.character)
    assertNull(castItem.creditId)
    assertEquals(castItem.mediaType, "movie")
    assertNull(castItem.originalName)
    assertEquals(castItem.popularity, 0.0, 0.0001)
    assertEquals(castItem.voteAverage, 0f)
    assertNull(castItem.name)
    assertEquals(castItem.id, 0)
    assertFalse(castItem.adult)
    assertEquals(castItem.voteCount, 0)
    assertNull(castItem.originalTitle)
    assertFalse(castItem.video)
    assertNull(castItem.title)
    assertNull(castItem.releaseDate)
    assertEquals(castItem.order, 0)
  }
}
