package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.models.GenresItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.ads
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.buy
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.flatrate
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.free
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.fullMovieDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywordsItems
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.rent
import com.waffiq.bazz_movies.feature.detail.testutils.MapperHelperTest.stubToMediaDetail
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class BasicMovieDetailMapperTest {

  private val validMovieDetail = fullMovieDetail.stubToMediaDetail()

  @Test
  fun movieDetail_validValue_returnsCorrectValue() {
    assertEquals(1, validMovieDetail.id)
    assertEquals(mediaKeywordsItems, validMovieDetail.keywords)
    assertNotNull(validMovieDetail.duration)
    assertEquals("$1,000,000.00", validMovieDetail.budget)
    assertEquals("$5,000,000.00", validMovieDetail.revenue)
    assertEquals("8.0", validMovieDetail.tmdbScore)
    assertEquals("Action, Comedy", validMovieDetail.genre)
    assertEquals("superhero", validMovieDetail.keywords?.get(0)?.name)
    assertEquals("English", validMovieDetail.language)
    assertEquals("Link Trailer", validMovieDetail.trailer)
    assertEquals(
      WatchProvidersUiState.Success(ads, buy, flatrate, free, rent),
      validMovieDetail.watchProviders,
    )
    assertEquals(listOf(1, 2), validMovieDetail.genreId)
    assertEquals("tt9999999", validMovieDetail.imdbId)
    assertEquals("Released", validMovieDetail.status)
    assertEquals(null, validMovieDetail.totalSeasons)
    assertEquals(null, validMovieDetail.totalEpisodes)
    assertEquals(4444.0f, validMovieDetail.popularity)
    assertEquals("2025-07-01", validMovieDetail.releaseDate)
  }

  @Test
  fun movieDetail_nullValue_returnsCorrectly() {
    val result = fullMovieDetail.copy(
      id = null,
      runtime = null,
      budget = null,
      revenue = null,
      voteAverage = null,
      listGenres = null,
      keywords = null,
      originalLanguage = null,
      imdbId = null,
      status = null,
      popularity = null,
      videos = null,
      watchProviders = null,
      releaseDate = null,
    ).stubToMediaDetail()

    assertEquals(0, result.id)
    assertNull(result.duration)
    assertEquals("-", result.budget)
    assertEquals("-", result.revenue)
    assertNull(result.tmdbScore)
    assertNull(result.genre)
    assertNull(result.keywords)
    assertNull(result.genreId)
    assertEquals("", result.language)
    assertNull(result.imdbId)
    assertNull(result.status)
    assertEquals(0f, result.popularity)
    assertNull(result.trailer)
    assertEquals(
      WatchProvidersUiState.Error("No watch providers available"),
      result.watchProviders,
    )
    assertEquals("", result.releaseDate)
  }

  @Test
  fun movieDetail_withZeroForNumericValue_returnsCorrectly() {
    val result = fullMovieDetail.copy(
      runtime = 0,
      budget = 0,
      revenue = 0L,
      voteAverage = 0.0,
      listGenres = emptyList(),
    ).stubToMediaDetail()

    assertNull(result.duration)
    assertEquals("-", result.budget)
    assertEquals("-", result.revenue)
    assertNull(result.tmdbScore)
    assertNull(result.genre)
    assertEquals(emptyList<Int>(), result.genreId)
  }

  @Test
  fun movieDetail_genresWithNullItem_nullItemIsSkippedInGenreString() {
    val genresWithNull = listOf(GenresItem(id = 2, name = "Comedy"), null)
    val result = fullMovieDetail.copy(listGenres = genresWithNull).stubToMediaDetail()
    assertEquals("Comedy", result.genre)
    assertEquals(listOf(2, 0), result.genreId) // null item id elvis to 0
  }

  @Test
  fun movieDetail_languageUnknownCode_returnsEmpty() {
    val result = fullMovieDetail.copy(originalLanguage = "zz").stubToMediaDetail()
    assertEquals("", result.language)
  }
}
