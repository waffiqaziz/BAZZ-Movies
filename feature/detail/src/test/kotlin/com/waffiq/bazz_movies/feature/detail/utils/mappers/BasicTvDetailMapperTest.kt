package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.models.GenresItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.ads
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.buy
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.flatrate
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.free
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.fullTvDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywordsItems
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.rent
import com.waffiq.bazz_movies.feature.detail.testutils.MapperHelperTest.stubToMediaDetail
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BasicTvDetailMapperTest {

  private val validTvDetail = fullTvDetail.stubToMediaDetail()

  @Test
  fun tvDetail_validValue_returnsCorrectValue() {
    assertEquals(1, validTvDetail.id)
    assertEquals("tt1234567", validTvDetail.imdbId)
    assertEquals("7.5", validTvDetail.tmdbScore)
    assertEquals("Action, Comedy", validTvDetail.genre)
    assertEquals(mediaKeywordsItems, validTvDetail.keywords)
    assertEquals("English", validTvDetail.language)
    assertEquals("Link Trailer", validTvDetail.trailer)
    assertEquals(
      WatchProvidersUiState.Success(ads, buy, flatrate, free, rent),
      validTvDetail.watchProviders,
    )
    assertEquals(listOf(1, 2), validTvDetail.genreId)

    // budget and revenue always return null for tv media type
    assertEquals(null, validTvDetail.budget)
    assertEquals(null, validTvDetail.revenue)

    assertEquals("Returning Series", validTvDetail.status)
    assertEquals(4, validTvDetail.totalSeasons)
    assertEquals(96, validTvDetail.totalEpisodes)
    assertEquals("2020-01-01", validTvDetail.releaseDate)
  }

  @Test
  fun tvDetail_nullValue_returnsCorrectly() {
    val result = fullTvDetail.copy(
      id = null,
      voteAverage = null,
      listGenres = null,
      keywords = null,
      originalLanguage = null,
      status = null,
      numberOfSeasons = null,
      numberOfEpisodes = null,
      popularity = null,
      videos = null,
      watchProviders = null,
      externalIds = null,
      backdropPath = null,
      posterPath = null,
      overview = null,
      firstAirDate = null,
    ).stubToMediaDetail()

    assertEquals(0, result.id)
    assertNull(result.tmdbScore)
    assertNull(result.genre)
    assertNull(result.genreId)
    assertNull(result.keywords)
    assertEquals("", result.imdbId)
    assertEquals("", result.language)
    assertNull(result.status)
    assertNull(result.totalSeasons)
    assertNull(result.totalEpisodes)
    assertEquals(9.0f, validTvDetail.popularity)
    assertEquals(0f, result.popularity)
    assertNull(result.trailer)
    assertEquals(
      WatchProvidersUiState.Error("No watch providers available"),
      result.watchProviders,
    )
    assertEquals("", result.backdrop)
    assertEquals("", result.poster)
    assertEquals("", result.overview)
    assertEquals("", result.releaseDate)
  }

  @Test
  fun tvDetail_voteAverageZero_returnsNullTmdbScore() {
    val result = fullTvDetail.copy(voteAverage = 0.0).stubToMediaDetail()
    assertNull(result.tmdbScore)
  }

  @Test
  fun tvDetail_genresEmpty_returnsNullGenre() {
    val result = fullTvDetail.copy(listGenres = emptyList()).stubToMediaDetail()
    assertNull(result.genre)
    assertEquals(emptyList<Int>(), result.genreId)
  }

  @Test
  fun tvDetail_genresWithNullItem_nullItemIsSkippedInGenreString() {
    val genresWithNull = listOf(GenresItem(id = 1, name = "Action"), null)
    val result = fullTvDetail.copy(listGenres = genresWithNull).stubToMediaDetail()

    assertEquals("Action", result.genre)
    assertEquals(listOf(1, 0), result.genreId)
  }

  @Test
  fun tvDetail_languageUnknownCode_returnsEmpty() {
    val result = fullTvDetail.copy(originalLanguage = "xx").stubToMediaDetail()
    assertEquals("No Language", result.language)
  }
}
