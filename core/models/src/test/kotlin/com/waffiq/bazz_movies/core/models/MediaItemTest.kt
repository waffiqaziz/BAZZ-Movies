package com.waffiq.bazz_movies.core.models

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MediaItemTest {

  private val mediaItemValid = MediaItem(
    firstAirDate = "2023-01-01",
    overview = "A thrilling movie about testing.",
    originalLanguage = "en",
    listGenreIds = listOf(28, 12),
    posterPath = "/poster.jpg",
    backdropPath = "/backdrop.jpg",
    mediaType = "movie",
    originalName = "Original Name",
    popularity = 123.45,
    voteAverage = 8.7f,
    name = "Test Movie",
    id = 42,
    adult = true,
    voteCount = 789,
    originalTitle = "Original Title",
    video = true,
    title = "Test Title",
    releaseDate = "2023-01-01",
    originCountry = listOf("US", "CA"),
  )
  private val mediaItemNull = MediaItem()

  @Test
  fun mediaItem_withValidValue_returnsCorrectData() {
    assertEquals("2023-01-01", mediaItemValid.firstAirDate)
    assertEquals("A thrilling movie about testing.", mediaItemValid.overview)
    assertEquals("en", mediaItemValid.originalLanguage)
    assertEquals(listOf(28, 12), mediaItemValid.listGenreIds)
    assertEquals("/poster.jpg", mediaItemValid.posterPath)
    assertEquals("/backdrop.jpg", mediaItemValid.backdropPath)
    assertEquals("movie", mediaItemValid.mediaType)
    assertEquals("Original Name", mediaItemValid.originalName)
    assertEquals(123.45, mediaItemValid.popularity)
    assertEquals(8.7f, mediaItemValid.voteAverage)
    assertEquals("Test Movie", mediaItemValid.name)
    assertEquals(42, mediaItemValid.id)
    assertTrue(mediaItemValid.adult)
    assertEquals(789, mediaItemValid.voteCount)
    assertEquals("Original Title", mediaItemValid.originalTitle)
    assertTrue(mediaItemValid.video)
    assertEquals("Test Title", mediaItemValid.title)
    assertEquals("2023-01-01", mediaItemValid.releaseDate)
    assertEquals(listOf("US", "CA"), mediaItemValid.originCountry)
  }

  @Test
  fun mediaItem_withNullValue_returnsCorrectData() {
    assertNull(mediaItemNull.firstAirDate)
    assertNull(mediaItemNull.overview)
    assertNull(mediaItemNull.originalLanguage)
    assertNull(mediaItemNull.listGenreIds)
    assertNull(mediaItemNull.posterPath)
    assertNull(mediaItemNull.backdropPath)
    assertEquals("movie", mediaItemNull.mediaType)
    assertNull(mediaItemNull.originalName)
    assertEquals(0.0, mediaItemNull.popularity)
    assertEquals(0f, mediaItemNull.voteAverage)
    assertNull(mediaItemNull.name)
    assertEquals(0, mediaItemNull.id)
    assertFalse(mediaItemNull.adult)
    assertEquals(0, mediaItemNull.voteCount)
    assertNull(mediaItemNull.originalTitle)
    assertFalse(mediaItemNull.video)
    assertNull(mediaItemNull.title)
    assertNull(mediaItemNull.releaseDate)
    assertNull(mediaItemNull.originCountry)
  }
}
