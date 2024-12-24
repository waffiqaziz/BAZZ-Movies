package com.waffiq.bazz_movies.feature.search.utils

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.KnownForItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.feature.search.utils.SearchMapper.toResultItemSearch
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SearchMapperTest {

  @Test
  fun `toResultItemSearch should map ResultsItemSearchResponse to ResultsItemSearch correctly`() {
    val response = ResultsItemSearchResponse(
      mediaType = "movie",
      listKnownFor = listOf(
        KnownForItemResponse(title = "Known Movie 1"),
        KnownForItemResponse(title = "Known Movie 2")
      ),
      knownForDepartment = "Acting",
      popularity = 8.5,
      name = "John Doe",
      profilePath = "/profile.jpg",
      id = 123,
      adult = false,
      overview = "Sample Overview",
      originalLanguage = "en",
      originalTitle = "Original Title",
      video = true,
      title = "Movie Title",
      listGenreIds = listOf(1, 2, 3),
      posterPath = "/poster.jpg",
      backdropPath = "/backdrop.jpg",
      releaseDate = "2024-01-01",
      voteAverage = 7.8,
      voteCount = 1500.0,
      firstAirDate = "2023-12-01",
      listOriginCountry = listOf("US"),
      originalName = "Original Movie Name"
    )

    val result = response.toResultItemSearch()

    assertEquals("movie", result.mediaType)
    assertEquals(2, result.listKnownFor?.size)
    assertEquals("Known Movie 1", result.listKnownFor?.get(0)?.title)
    assertEquals("Known Movie 2", result.listKnownFor?.get(1)?.title)
    assertEquals("Acting", result.knownForDepartment)
    assertEquals(8.5, result.popularity, 0.0)
    assertEquals("John Doe", result.name)
    assertEquals("/profile.jpg", result.profilePath)
    assertEquals(123, result.id)
    assertFalse(result.adult)
    assertEquals("Sample Overview", result.overview)
    assertEquals("en", result.originalLanguage)
    assertEquals("Original Title", result.originalTitle)
    assertTrue(result.video)
    assertEquals("Movie Title", result.title)
    assertEquals(listOf(1, 2, 3), result.listGenreIds)
    assertEquals("/poster.jpg", result.posterPath)
    assertEquals("/backdrop.jpg", result.backdropPath)
    assertEquals("2024-01-01", result.releaseDate)
    assertEquals(7.8, result.voteAverage, 0.0)
    assertEquals(1500.0, result.voteCount, 0.0)
    assertEquals("2023-12-01", result.firstAirDate)
    assertEquals(listOf("US"), result.listOriginCountry)
    assertEquals("Original Movie Name", result.originalName)
  }
}
