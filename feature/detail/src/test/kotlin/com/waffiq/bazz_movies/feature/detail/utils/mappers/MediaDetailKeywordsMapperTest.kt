package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.fullMovieDetail
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.fullTvDetail
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.mockKeywordsItems
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.mockMediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.mockReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.mockTvExternalIds
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaDetail
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MediaDetailKeywordsMapperTest {

  // region TvDetail.toMediaDetail

  // id branches: non-null & null

  @Test
  fun tvDetail_idNonNull_returnsActualId() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals(1, result.id)
  }

  @Test
  fun tvDetail_idNull_returnsZero() {
    val result =
      fullTvDetail.copy(id = null).toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals(0, result.id)
  }

  // externalIds branches: non-null, null object, null imdbId inside

  @Test
  fun tvDetail_externalIdsNonNull_returnsImdbId() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("tt1234567", result.imdbId)
  }

  @Test
  fun tvDetail_externalIdsNull_returnsEmptyImdbId() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, externalIds = null)
    assertEquals("", result.imdbId)
  }

  @Test
  fun tvDetail_externalIdsImdbIdNull_returnsEmptyImdbId() {
    val externalIdsWithNullImdb = mockTvExternalIds.copy(imdbId = null)
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, externalIdsWithNullImdb)
    assertEquals("", result.imdbId)
  }

  // mediaKeywords branches: non-null, null, non-null with null keywords list

  @Test
  fun tvDetail_mediaKeywordsNonNull_returnsKeywords() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals(mockKeywordsItems, result.keywords)
  }

  @Test
  fun tvDetail_mediaKeywordsNull_returnsNullKeywords() {
    val result =
      fullTvDetail.toMediaDetail("US", mediaKeywords = null, externalIds = mockTvExternalIds)
    assertNull(result.keywords)
  }

  @Test
  fun tvDetail_mediaKeywordsWithNullKeywordsList_returnsNull() {
    val keywordsWithNullList = MediaKeywords(id = 100, keywords = null)
    val result = fullTvDetail.toMediaDetail("US", keywordsWithNullList, mockTvExternalIds)
    assertNull(result.keywords)
  }

  // voteAverage branches: positive, zero, null

  @Test
  fun tvDetail_voteAveragePositive_returnsTmdbScore() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("7.5", result.tmdbScore)
  }

  @Test
  fun tvDetail_voteAverageZero_returnsNullTmdbScore() {
    val result =
      fullTvDetail.copy(voteAverage = 0.0).toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertNull(result.tmdbScore)
  }

  @Test
  fun tvDetail_voteAverageNull_returnsNullTmdbScore() {
    val result = fullTvDetail.copy(voteAverage = null)
      .toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertNull(result.tmdbScore)
  }

  // listGenres branches: valid list, null, empty, list with null item

  @Test
  fun tvDetail_genresValidList_returnsGenreString() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("Action, Comedy", result.genre)
    assertEquals(listOf(1, 2), result.genreId)
  }

  @Test
  fun tvDetail_genresNull_returnsNullGenre() {
    val result =
      fullTvDetail.copy(listGenres = null).toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertNull(result.genre)
    assertNull(result.genreId)
  }

  @Test
  fun tvDetail_genresEmpty_returnsNullGenre() {
    val result = fullTvDetail.copy(listGenres = emptyList())
      .toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertNull(result.genre)
    assertEquals(emptyList<Int>(), result.genreId)
  }

  @Test
  fun tvDetail_genresWithNullItem_nullItemIsSkippedInGenreString() {
    val genresWithNull = listOf(GenresItem(id = 1, name = "Action"), null)
    val result = fullTvDetail.copy(listGenres = genresWithNull)
      .toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("Action", result.genre)
    assertEquals(listOf(1, 0), result.genreId) // null item id elvis to 0
  }

  // originalLanguage branches: known code, unknown code, null

  @Test
  fun tvDetail_languageKnownCode_returnsLanguageName() {
    val result = fullTvDetail.copy(originalLanguage = "de")
      .toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("German", result.language)
  }

  @Test
  fun tvDetail_languageUnknownCode_returnsEmpty() {
    val result = fullTvDetail.copy(originalLanguage = "xx")
      .toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("No Language", result.language)
  }

  @Test
  fun tvDetail_languageNull_returnsEmpty() {
    val result = fullTvDetail.copy(originalLanguage = null)
      .toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("", result.language)
  }

  // budget and revenue are always hardcoded to "-"

  @Test
  fun tvDetail_budgetAndRevenue_alwaysReturnDash() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("-", result.budget)
    assertEquals("-", result.revenue)
  }

  // status passthrough

  @Test
  fun tvDetail_statusNonNull_returnsStatus() {
    val result = fullTvDetail.toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertEquals("Returning Series", result.status)
  }

  @Test
  fun tvDetail_statusNull_returnsNull() {
    val result =
      fullTvDetail.copy(status = null).toMediaDetail("US", mockMediaKeywords, mockTvExternalIds)
    assertNull(result.status)
  }

  // endregion

  // region MovieDetail.toMediaDetail
  // id branches: non-null & null

  @Test
  fun movieDetail_idNonNull_returnsActualId() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals(1, result.id)
  }

  @Test
  fun movieDetail_idNull_returnsZero() {
    val result =
      fullMovieDetail.copy(id = null).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals(0, result.id)
  }

  // mediaKeywords branches: non-null, null, non-null with null keywords list

  @Test
  fun movieDetail_mediaKeywordsNonNull_returnsKeywords() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals(mockKeywordsItems, result.keywords)
  }

  @Test
  fun movieDetail_mediaKeywordsNull_returnsNullKeywords() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mediaKeywords = null)
    assertNull(result.keywords)
  }

  @Test
  fun movieDetail_mediaKeywordsWithNullKeywordsList_returnsNull() {
    val keywordsWithNullList = MediaKeywords(id = 100, keywords = null)
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, keywordsWithNullList)
    assertNull(result.keywords)
  }

  // runtime branches: valid, zero, null

  @Test
  fun movieDetail_runtimeValid_returnsDuration() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNotNull(result.duration) // e.g. "2h 0m" depending on convertRuntime
  }

  @Test
  fun movieDetail_runtimeZero_returnsNullDuration() {
    val result =
      fullMovieDetail.copy(runtime = 0).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.duration)
  }

  @Test
  fun movieDetail_runtimeNull_returnsNullDuration() {
    val result =
      fullMovieDetail.copy(runtime = null).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.duration)
  }

  // budget branches: valid, zero, null

  @Test
  fun movieDetail_budgetValid_returnsFormattedUsd() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("$1,000,000.00", result.budget)
  }

  @Test
  fun movieDetail_budgetZero_returnsDash() {
    val result =
      fullMovieDetail.copy(budget = 0).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("-", result.budget)
  }

  @Test
  fun movieDetail_budgetNull_returnsDash() {
    val result =
      fullMovieDetail.copy(budget = null).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("-", result.budget)
  }

  // revenue branches: valid, zero, null

  @Test
  fun movieDetail_revenueValid_returnsFormattedUsd() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("$5,000,000.00", result.revenue)
  }

  @Test
  fun movieDetail_revenueZero_returnsDash() {
    val result =
      fullMovieDetail.copy(revenue = 0L).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("-", result.revenue)
  }

  @Test
  fun movieDetail_revenueNull_returnsDash() {
    val result =
      fullMovieDetail.copy(revenue = null).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("-", result.revenue)
  }

  // voteAverage branches: positive, zero, null

  @Test
  fun movieDetail_voteAveragePositive_returnsTmdbScore() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("8.0", result.tmdbScore)
  }

  @Test
  fun movieDetail_voteAverageZero_returnsNullTmdbScore() {
    val result = fullMovieDetail.copy(voteAverage = 0.0)
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.tmdbScore)
  }

  @Test
  fun movieDetail_voteAverageNull_returnsNullTmdbScore() {
    val result = fullMovieDetail.copy(voteAverage = null)
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.tmdbScore)
  }

  // listGenres branches: valid list, null, empty, list with null item

  @Test
  fun movieDetail_genresValidList_returnsGenreString() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("Action, Comedy", result.genre)
    assertEquals(listOf(1, 2), result.genreId)
  }

  @Test
  fun movieDetail_genresNull_returnsNullGenre() {
    val result = fullMovieDetail.copy(listGenres = null)
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.genre)
    assertNull(result.genreId)
  }

  @Test
  fun movieDetail_genresEmpty_returnsNullGenre() {
    val result = fullMovieDetail.copy(listGenres = emptyList())
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.genre)
    assertEquals(emptyList<Int>(), result.genreId)
  }

  @Test
  fun movieDetail_genresWithNullItem_nullItemIsSkippedInGenreString() {
    val genresWithNull = listOf(GenresItem(id = 2, name = "Comedy"), null)
    val result = fullMovieDetail.copy(listGenres = genresWithNull)
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("Comedy", result.genre)
    assertEquals(listOf(2, 0), result.genreId) // null item id elvis to 0
  }

  // originalLanguage branches: known code, unknown code, null

  @Test
  fun movieDetail_languageKnownCode_returnsLanguageName() {
    val result = fullMovieDetail.copy(originalLanguage = "cs")
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("Czech", result.language)
  }

  @Test
  fun movieDetail_languageUnknownCode_returnsEmpty() {
    val result = fullMovieDetail.copy(originalLanguage = "zz")
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("", result.language)
  }

  @Test
  fun movieDetail_languageNull_returnsEmpty() {
    val result = fullMovieDetail.copy(originalLanguage = null)
      .toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("", result.language)
  }

  // imdbId passthrough (movie reads directly from MovieDetail, not externalIds)

  @Test
  fun movieDetail_imdbIdNonNull_returnsImdbId() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("tt9999999", result.imdbId)
  }

  @Test
  fun movieDetail_imdbIdNull_returnsNull() {
    val result =
      fullMovieDetail.copy(imdbId = null).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.imdbId)
  }

  // status passthrough

  @Test
  fun movieDetail_statusNonNull_returnsStatus() {
    val result = fullMovieDetail.toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertEquals("Released", result.status)
  }

  @Test
  fun movieDetail_statusNull_returnsNull() {
    val result =
      fullMovieDetail.copy(status = null).toMediaDetail(mockReleaseDateRegion, mockMediaKeywords)
    assertNull(result.status)
  }

  // endregion
}
