package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.fullMovieDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.fullTvDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywordsItems
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.releaseDateRegion
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.tvExternalIds
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
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals(1, result.id)
  }

  @Test
  fun tvDetail_idNull_returnsZero() {
    val result =
      fullTvDetail.copy(id = null).toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals(0, result.id)
  }

  // externalIds branches: non-null, null object, null imdbId inside

  @Test
  fun tvDetail_externalIdsNonNull_returnsImdbId() {
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("tt1234567", result.imdbId)
  }

  @Test
  fun tvDetail_externalIdsNull_returnsEmptyImdbId() {
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, externalIds = null)
    assertEquals("", result.imdbId)
  }

  @Test
  fun tvDetail_externalIdsImdbIdNull_returnsEmptyImdbId() {
    val externalIdsWithNullImdb = tvExternalIds.copy(imdbId = null)
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, externalIdsWithNullImdb)
    assertEquals("", result.imdbId)
  }

  // mediaKeywords branches: non-null, null, non-null with null keywords list

  @Test
  fun tvDetail_mediaKeywordsNonNull_returnsKeywords() {
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals(mediaKeywordsItems, result.keywords)
  }

  @Test
  fun tvDetail_mediaKeywordsNull_returnsNullKeywords() {
    val result =
      fullTvDetail.toMediaDetail("US", mediaKeywords = null, externalIds = tvExternalIds)
    assertNull(result.keywords)
  }

  @Test
  fun tvDetail_mediaKeywordsWithNullKeywordsList_returnsNull() {
    val keywordsWithNullList = MediaKeywords(id = 100, keywords = null)
    val result = fullTvDetail.toMediaDetail("US", keywordsWithNullList, tvExternalIds)
    assertNull(result.keywords)
  }

  // voteAverage branches: positive, zero, null

  @Test
  fun tvDetail_voteAveragePositive_returnsTmdbScore() {
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("7.5", result.tmdbScore)
  }

  @Test
  fun tvDetail_voteAverageZero_returnsNullTmdbScore() {
    val result =
      fullTvDetail.copy(voteAverage = 0.0).toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertNull(result.tmdbScore)
  }

  @Test
  fun tvDetail_voteAverageNull_returnsNullTmdbScore() {
    val result = fullTvDetail.copy(voteAverage = null)
      .toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertNull(result.tmdbScore)
  }

  // listGenres branches: valid list, null, empty, list with null item

  @Test
  fun tvDetail_genresValidList_returnsGenreString() {
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("Action, Comedy", result.genre)
    assertEquals(listOf(1, 2), result.genreId)
  }

  @Test
  fun tvDetail_genresNull_returnsNullGenre() {
    val result =
      fullTvDetail.copy(listGenres = null).toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertNull(result.genre)
    assertNull(result.genreId)
  }

  @Test
  fun tvDetail_genresEmpty_returnsNullGenre() {
    val result = fullTvDetail.copy(listGenres = emptyList())
      .toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertNull(result.genre)
    assertEquals(emptyList<Int>(), result.genreId)
  }

  @Test
  fun tvDetail_genresWithNullItem_nullItemIsSkippedInGenreString() {
    val genresWithNull = listOf(GenresItem(id = 1, name = "Action"), null)
    val result = fullTvDetail.copy(listGenres = genresWithNull)
      .toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("Action", result.genre)
    assertEquals(listOf(1, 0), result.genreId) // null item id elvis to 0
  }

  // originalLanguage branches: known code, unknown code, null

  @Test
  fun tvDetail_languageKnownCode_returnsLanguageName() {
    val result = fullTvDetail.copy(originalLanguage = "de")
      .toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("German", result.language)
  }

  @Test
  fun tvDetail_languageUnknownCode_returnsEmpty() {
    val result = fullTvDetail.copy(originalLanguage = "xx")
      .toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("No Language", result.language)
  }

  @Test
  fun tvDetail_languageNull_returnsEmpty() {
    val result = fullTvDetail.copy(originalLanguage = null)
      .toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("", result.language)
  }

  // budget and revenue are always hardcoded to "-"

  @Test
  fun tvDetail_budgetAndRevenue_alwaysReturnDash() {
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("-", result.budget)
    assertEquals("-", result.revenue)
  }

  // status passthrough

  @Test
  fun tvDetail_statusNonNull_returnsStatus() {
    val result = fullTvDetail.toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertEquals("Returning Series", result.status)
  }

  @Test
  fun tvDetail_statusNull_returnsNull() {
    val result =
      fullTvDetail.copy(status = null).toMediaDetail("US", mediaKeywords, tvExternalIds)
    assertNull(result.status)
  }

  // endregion

  // region MovieDetail.toMediaDetail
  // id branches: non-null & null

  @Test
  fun movieDetail_idNonNull_returnsActualId() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals(1, result.id)
  }

  @Test
  fun movieDetail_idNull_returnsZero() {
    val result =
      fullMovieDetail.copy(id = null).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals(0, result.id)
  }

  // mediaKeywords branches: non-null, null, non-null with null keywords list

  @Test
  fun movieDetail_mediaKeywordsNonNull_returnsKeywords() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals(mediaKeywordsItems, result.keywords)
  }

  @Test
  fun movieDetail_mediaKeywordsNull_returnsNullKeywords() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords = null)
    assertNull(result.keywords)
  }

  @Test
  fun movieDetail_mediaKeywordsWithNullKeywordsList_returnsNull() {
    val keywordsWithNullList = MediaKeywords(id = 100, keywords = null)
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, keywordsWithNullList)
    assertNull(result.keywords)
  }

  // runtime branches: valid, zero, null

  @Test
  fun movieDetail_runtimeValid_returnsDuration() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNotNull(result.duration) // e.g. "2h 0m" depending on convertRuntime
  }

  @Test
  fun movieDetail_runtimeZero_returnsNullDuration() {
    val result =
      fullMovieDetail.copy(runtime = 0).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.duration)
  }

  @Test
  fun movieDetail_runtimeNull_returnsNullDuration() {
    val result =
      fullMovieDetail.copy(runtime = null).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.duration)
  }

  // budget branches: valid, zero, null

  @Test
  fun movieDetail_budgetValid_returnsFormattedUsd() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("$1,000,000.00", result.budget)
  }

  @Test
  fun movieDetail_budgetZero_returnsDash() {
    val result =
      fullMovieDetail.copy(budget = 0).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("-", result.budget)
  }

  @Test
  fun movieDetail_budgetNull_returnsDash() {
    val result =
      fullMovieDetail.copy(budget = null).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("-", result.budget)
  }

  // revenue branches: valid, zero, null

  @Test
  fun movieDetail_revenueValid_returnsFormattedUsd() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("$5,000,000.00", result.revenue)
  }

  @Test
  fun movieDetail_revenueZero_returnsDash() {
    val result =
      fullMovieDetail.copy(revenue = 0L).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("-", result.revenue)
  }

  @Test
  fun movieDetail_revenueNull_returnsDash() {
    val result =
      fullMovieDetail.copy(revenue = null).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("-", result.revenue)
  }

  // voteAverage branches: positive, zero, null

  @Test
  fun movieDetail_voteAveragePositive_returnsTmdbScore() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("8.0", result.tmdbScore)
  }

  @Test
  fun movieDetail_voteAverageZero_returnsNullTmdbScore() {
    val result = fullMovieDetail.copy(voteAverage = 0.0)
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.tmdbScore)
  }

  @Test
  fun movieDetail_voteAverageNull_returnsNullTmdbScore() {
    val result = fullMovieDetail.copy(voteAverage = null)
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.tmdbScore)
  }

  // listGenres branches: valid list, null, empty, list with null item

  @Test
  fun movieDetail_genresValidList_returnsGenreString() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("Action, Comedy", result.genre)
    assertEquals(listOf(1, 2), result.genreId)
  }

  @Test
  fun movieDetail_genresNull_returnsNullGenre() {
    val result = fullMovieDetail.copy(listGenres = null)
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.genre)
    assertNull(result.genreId)
  }

  @Test
  fun movieDetail_genresEmpty_returnsNullGenre() {
    val result = fullMovieDetail.copy(listGenres = emptyList())
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.genre)
    assertEquals(emptyList<Int>(), result.genreId)
  }

  @Test
  fun movieDetail_genresWithNullItem_nullItemIsSkippedInGenreString() {
    val genresWithNull = listOf(GenresItem(id = 2, name = "Comedy"), null)
    val result = fullMovieDetail.copy(listGenres = genresWithNull)
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("Comedy", result.genre)
    assertEquals(listOf(2, 0), result.genreId) // null item id elvis to 0
  }

  // originalLanguage branches: known code, unknown code, null

  @Test
  fun movieDetail_languageKnownCode_returnsLanguageName() {
    val result = fullMovieDetail.copy(originalLanguage = "cs")
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("Czech", result.language)
  }

  @Test
  fun movieDetail_languageUnknownCode_returnsEmpty() {
    val result = fullMovieDetail.copy(originalLanguage = "zz")
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("", result.language)
  }

  @Test
  fun movieDetail_languageNull_returnsEmpty() {
    val result = fullMovieDetail.copy(originalLanguage = null)
      .toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("", result.language)
  }

  // imdbId passthrough (movie reads directly from MovieDetail, not externalIds)

  @Test
  fun movieDetail_imdbIdNonNull_returnsImdbId() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("tt9999999", result.imdbId)
  }

  @Test
  fun movieDetail_imdbIdNull_returnsNull() {
    val result =
      fullMovieDetail.copy(imdbId = null).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.imdbId)
  }

  // status passthrough

  @Test
  fun movieDetail_statusNonNull_returnsStatus() {
    val result = fullMovieDetail.toMediaDetail(releaseDateRegion, mediaKeywords)
    assertEquals("Released", result.status)
  }

  @Test
  fun movieDetail_statusNull_returnsNull() {
    val result =
      fullMovieDetail.copy(status = null).toMediaDetail(releaseDateRegion, mediaKeywords)
    assertNull(result.status)
  }

  // endregion
}
