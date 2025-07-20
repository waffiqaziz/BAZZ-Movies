package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaItemMapperTest {

  @Test
  fun toMediaItem_withValidValues_returnsMediaItem() {
    val mediaItemResponse = MediaResponseItem(
      firstAirDate = "2023-12-01",
      overview = "This is an overview",
      originalLanguage = "en",
      genreIds = listOf(1, 2, 3),
      posterPath = "/poster.jpg",
      backdropPath = "/backdrop.jpg",
      mediaType = "tv",
      originalName = "Original Name",
      popularity = 123.45,
      voteAverage = 8.9f,
      name = "Name",
      id = 42,
      adult = true,
      voteCount = 100,
      originalTitle = "Original Title",
      video = true,
      title = "Title",
      releaseDate = "2023-12-01",
      originCountry = listOf("ID", "MY")
    )

    val mediaItem = mediaItemResponse.toMediaItem()

    assertEquals("2023-12-01", mediaItem.firstAirDate)
    assertEquals("This is an overview", mediaItem.overview)
    assertEquals("en", mediaItem.originalLanguage)
    assertEquals(listOf(1, 2, 3), mediaItem.listGenreIds)
    assertEquals("/poster.jpg", mediaItem.posterPath)
    assertEquals("/backdrop.jpg", mediaItem.backdropPath)
    assertEquals("tv", mediaItem.mediaType)
    assertEquals("Original Name", mediaItem.originalName)
    assertEquals(123.45, mediaItem.popularity)
    assertEquals(8.9f, mediaItem.voteAverage)
    assertEquals("Name", mediaItem.name)
    assertEquals(42, mediaItem.id)
    assertEquals(true, mediaItem.adult)
    assertEquals(100, mediaItem.voteCount)
    assertEquals("Original Title", mediaItem.originalTitle)
    assertEquals(true, mediaItem.video)
    assertEquals("Title", mediaItem.title)
    assertEquals("2023-12-01", mediaItem.releaseDate)
    assertEquals(listOf("ID", "MY"), mediaItem.originCountry)
  }

  @Test
  fun toMediaItem_withNullValues_returnsMediaItem() {
    val mediaItemResponse = MediaResponseItem()

    val mediaItem = mediaItemResponse.toMediaItem()

    assertNull(mediaItem.firstAirDate)
    assertNull(mediaItem.overview)
    assertNull(mediaItem.originalLanguage)
    assertNull(mediaItem.listGenreIds)
    assertNull(mediaItem.posterPath)
    assertNull(mediaItem.backdropPath)
    assertEquals("movie", mediaItem.mediaType) // default value
    assertNull(mediaItem.originalName)
    assertEquals(null, mediaItem.popularity) // default value
    assertEquals(null, mediaItem.voteAverage) // default value
    assertNull(mediaItem.name)
    assertEquals(0, mediaItem.id) // default value
    assertEquals(false, mediaItem.adult) // default value
    assertEquals(0, mediaItem.voteCount) // default value
    assertNull(mediaItem.originalTitle)
    assertEquals(false, mediaItem.video) // default value
    assertNull(mediaItem.title)
    assertNull(mediaItem.releaseDate)
    assertNull(mediaItem.originCountry)
  }

  @Test
  fun toMediaItem_withEmptyStringsAndZeroValues_returnsMediaItem() {
    val mediaItemResponse = MediaResponseItem(
      firstAirDate = "",
      overview = "",
      originalLanguage = "",
      genreIds = emptyList(),
      posterPath = "",
      backdropPath = "",
      mediaType = "",
      originalName = "",
      popularity = 0.0,
      voteAverage = 0f,
      name = "",
      id = 0,
      adult = false,
      voteCount = 0,
      originalTitle = "",
      video = false,
      title = "",
      releaseDate = "",
      originCountry = emptyList()
    )

    val mediaItem = mediaItemResponse.toMediaItem()

    assertEquals("", mediaItem.firstAirDate)
    assertEquals("", mediaItem.overview)
    assertEquals("", mediaItem.originalLanguage)
    assertEquals(emptyList<Int>(), mediaItem.listGenreIds)
    assertEquals("", mediaItem.posterPath)
    assertEquals("", mediaItem.backdropPath)
    assertEquals("", mediaItem.mediaType)
    assertEquals("", mediaItem.originalName)
    assertEquals(0.0, mediaItem.popularity)
    assertEquals(0f, mediaItem.voteAverage)
    assertEquals("", mediaItem.name)
    assertEquals(0, mediaItem.id)
    assertEquals(false, mediaItem.adult)
    assertEquals(0, mediaItem.voteCount)
    assertEquals("", mediaItem.originalTitle)
    assertEquals(false, mediaItem.video)
    assertEquals("", mediaItem.title)
    assertEquals("", mediaItem.releaseDate)
    assertEquals(emptyList<String>(), mediaItem.listGenreIds)
  }

  @Test
  fun toMediaItem_withBlankValues_returnsMediaItem() {
    val mediaItemResponse = MediaResponseItem(
      firstAirDate = "  ",
      overview = "  ",
      originalLanguage = "  ",
      posterPath = "  ",
      backdropPath = "  ",
      mediaType = "  ",
      originalName = "  ",
      name = "  ",
      originalTitle = "  ",
      title = "  ",
      releaseDate = "  "
    )

    val mediaItem = mediaItemResponse.toMediaItem()

    assertEquals("  ", mediaItem.firstAirDate)
    assertEquals("  ", mediaItem.overview)
    assertEquals("  ", mediaItem.originalLanguage)
    assertEquals("  ", mediaItem.posterPath)
    assertEquals("  ", mediaItem.backdropPath)
    assertEquals("  ", mediaItem.mediaType)
    assertEquals("  ", mediaItem.originalName)
    assertEquals("  ", mediaItem.name)
    assertEquals("  ", mediaItem.originalTitle)
    assertEquals("  ", mediaItem.title)
    assertEquals("  ", mediaItem.releaseDate)
  }

  @Test
  fun toMediaItem_withSomeNullValues_returnsMediaItem() {
    val mediaItemResponse = MediaResponseItem(
      firstAirDate = null,
      overview = "Overview",
      genreIds = null,
      mediaType = null,
      id = null,
      voteCount = null
    )

    val mediaItem = mediaItemResponse.toMediaItem()

    assertNull(mediaItem.firstAirDate)
    assertEquals("Overview", mediaItem.overview)
    assertNull(mediaItem.listGenreIds)
    assertEquals("movie", mediaItem.mediaType) // default value
    assertEquals(0, mediaItem.id) // default value
    assertEquals(0, mediaItem.voteCount) // default value
    assertNull(mediaItem.originCountry)
  }
}
