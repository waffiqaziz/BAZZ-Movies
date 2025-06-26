package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.mappers.ResultItemMapper.toResultItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResultItemMapperTest {

  @Test
  fun toResultItem_withValidValues_returnsResultItem() {
    val resultItemResponse = ResultItemResponse(
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

    val resultItem = resultItemResponse.toResultItem()

    assertEquals("2023-12-01", resultItem.firstAirDate)
    assertEquals("This is an overview", resultItem.overview)
    assertEquals("en", resultItem.originalLanguage)
    assertEquals(listOf(1, 2, 3), resultItem.listGenreIds)
    assertEquals("/poster.jpg", resultItem.posterPath)
    assertEquals("/backdrop.jpg", resultItem.backdropPath)
    assertEquals("tv", resultItem.mediaType)
    assertEquals("Original Name", resultItem.originalName)
    assertEquals(123.45, resultItem.popularity)
    assertEquals(8.9f, resultItem.voteAverage)
    assertEquals("Name", resultItem.name)
    assertEquals(42, resultItem.id)
    assertEquals(true, resultItem.adult)
    assertEquals(100, resultItem.voteCount)
    assertEquals("Original Title", resultItem.originalTitle)
    assertEquals(true, resultItem.video)
    assertEquals("Title", resultItem.title)
    assertEquals("2023-12-01", resultItem.releaseDate)
    assertEquals(listOf("ID", "MY"), resultItem.originCountry)
  }

  @Test
  fun toResultItem_withNullValues_returnsResultItem() {
    val resultItemResponse = ResultItemResponse()

    val resultItem = resultItemResponse.toResultItem()

    assertNull(resultItem.firstAirDate)
    assertNull(resultItem.overview)
    assertNull(resultItem.originalLanguage)
    assertNull(resultItem.listGenreIds)
    assertNull(resultItem.posterPath)
    assertNull(resultItem.backdropPath)
    assertEquals("movie", resultItem.mediaType) // default value
    assertNull(resultItem.originalName)
    assertEquals(null, resultItem.popularity) // default value
    assertEquals(null, resultItem.voteAverage) // default value
    assertNull(resultItem.name)
    assertEquals(0, resultItem.id) // default value
    assertEquals(false, resultItem.adult) // default value
    assertEquals(0, resultItem.voteCount) // default value
    assertNull(resultItem.originalTitle)
    assertEquals(false, resultItem.video) // default value
    assertNull(resultItem.title)
    assertNull(resultItem.releaseDate)
    assertNull(resultItem.originCountry)
  }

  @Test
  fun toResultItem_withEmptyStringsAndZeroValues_returnsResultItem() {
    val resultItemResponse = ResultItemResponse(
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

    val resultItem = resultItemResponse.toResultItem()

    assertEquals("", resultItem.firstAirDate)
    assertEquals("", resultItem.overview)
    assertEquals("", resultItem.originalLanguage)
    assertEquals(emptyList<Int>(), resultItem.listGenreIds)
    assertEquals("", resultItem.posterPath)
    assertEquals("", resultItem.backdropPath)
    assertEquals("", resultItem.mediaType)
    assertEquals("", resultItem.originalName)
    assertEquals(0.0, resultItem.popularity)
    assertEquals(0f, resultItem.voteAverage)
    assertEquals("", resultItem.name)
    assertEquals(0, resultItem.id)
    assertEquals(false, resultItem.adult)
    assertEquals(0, resultItem.voteCount)
    assertEquals("", resultItem.originalTitle)
    assertEquals(false, resultItem.video)
    assertEquals("", resultItem.title)
    assertEquals("", resultItem.releaseDate)
    assertEquals(emptyList<String>(), resultItem.listGenreIds)
  }

  @Test
  fun toResultItem_withBlankValues_returnsResultItem() {
    val resultItemResponse = ResultItemResponse(
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

    val resultItem = resultItemResponse.toResultItem()

    assertEquals("  ", resultItem.firstAirDate)
    assertEquals("  ", resultItem.overview)
    assertEquals("  ", resultItem.originalLanguage)
    assertEquals("  ", resultItem.posterPath)
    assertEquals("  ", resultItem.backdropPath)
    assertEquals("  ", resultItem.mediaType)
    assertEquals("  ", resultItem.originalName)
    assertEquals("  ", resultItem.name)
    assertEquals("  ", resultItem.originalTitle)
    assertEquals("  ", resultItem.title)
    assertEquals("  ", resultItem.releaseDate)
  }

  @Test
  fun toResultItem_withSomeNullValues_returnsResultItem() {
    val resultItemResponse = ResultItemResponse(
      firstAirDate = null,
      overview = "Overview",
      genreIds = null,
      mediaType = null,
      id = null,
      voteCount = null
    )

    val resultItem = resultItemResponse.toResultItem()

    assertNull(resultItem.firstAirDate)
    assertEquals("Overview", resultItem.overview)
    assertNull(resultItem.listGenreIds)
    assertEquals("movie", resultItem.mediaType) // default value
    assertEquals(0, resultItem.id) // default value
    assertEquals(0, resultItem.voteCount) // default value
    assertNull(resultItem.originCountry)
  }
}
