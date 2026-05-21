package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.models.GenresItem
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
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

  private val validTvDetail = fullTvDetail.stubToMediaDetail()
  private val validMovieDetail = fullMovieDetail.stubToMediaDetail()

  // region TvDetail.toMediaDetail

  // id branches: non-null & null

  @Test
  fun tvDetail_idNonNull_returnsActualId() {
    assertEquals(1, validTvDetail.id)
  }

  @Test
  fun tvDetail_idNull_returnsZero() {
    val result = fullTvDetail.copy(id = null).stubToMediaDetail()
    assertEquals(0, result.id)
  }

  // externalIds branches: non-null, null object, null imdbId inside

  @Test
  fun tvDetail_externalIdsNonNull_returnsImdbId() {
    assertEquals("tt1234567", validTvDetail.imdbId)
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
    assertEquals(mediaKeywordsItems, validTvDetail.keywords)
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
    assertEquals("7.5", validTvDetail.tmdbScore)
  }

  @Test
  fun tvDetail_voteAverageZero_returnsNullTmdbScore() {
    val result = fullTvDetail.copy(voteAverage = 0.0).stubToMediaDetail()
    assertNull(result.tmdbScore)
  }

  @Test
  fun tvDetail_voteAverageNull_returnsNullTmdbScore() {
    val result = fullTvDetail.copy(voteAverage = null).stubToMediaDetail()
    assertNull(result.tmdbScore)
  }

  // listGenres branches: valid list, null, empty, list with null item

  @Test
  fun tvDetail_genresValidList_returnsGenreString() {
    assertEquals("Action, Comedy", validTvDetail.genre)
    assertEquals(listOf(1, 2), validTvDetail.genreId)
  }

  @Test
  fun tvDetail_genresNull_returnsNullGenre() {
    val result = fullTvDetail.copy(listGenres = null).stubToMediaDetail()
    assertNull(result.genre)
    assertNull(result.genreId)
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
    assertEquals(listOf(1, 0), result.genreId) // null item id elvis to 0
  }

  // originalLanguage branches: known code, unknown code, null

  @Test
  fun tvDetail_languageKnownCode_returnsLanguageName() {
    val result = fullTvDetail.copy(originalLanguage = "de").stubToMediaDetail()
    assertEquals("German", result.language)
  }

  @Test
  fun tvDetail_languageUnknownCode_returnsEmpty() {
    val result = fullTvDetail.copy(originalLanguage = "xx").stubToMediaDetail()
    assertEquals("No Language", result.language)
  }

  @Test
  fun tvDetail_languageNull_returnsEmpty() {
    val result = fullTvDetail.copy(originalLanguage = null).stubToMediaDetail()
    assertEquals("", result.language)
  }

  // budget and revenue are always hardcoded to "-"

  @Test
  fun tvDetail_budgetAndRevenue_alwaysReturnNull() {
    assertEquals(null, validTvDetail.budget)
    assertEquals(null, validTvDetail.revenue)
  }

  // status passthrough

  @Test
  fun tvDetail_statusNonNull_returnsStatus() {
    assertEquals("Returning Series", validTvDetail.status)
  }

  @Test
  fun tvDetail_statusNull_returnsNull() {
    val result = fullTvDetail.copy(status = null).stubToMediaDetail()
    assertNull(result.status)
  }

  // total episode and season branches: non-null, null
  @Test
  fun tvDetail_totalEpisodeAndSeasonIsValid_returnsValueCorrectly() {
    assertEquals(4, validTvDetail.totalSeasons)
    assertEquals(96, validTvDetail.totalEpisodes)
  }

  @Test
  fun tvDetail_totalEpisodeAndSeasonIsNull_returnsNull() {
    val result = fullTvDetail.copy(status = null).stubToMediaDetail()
    assertNull(result.status)
  }

  // endregion

  // region MovieDetail.toMediaDetail
  // id branches: non-null & null

  @Test
  fun movieDetail_idNonNull_returnsActualId() {
    assertEquals(1, validMovieDetail.id)
  }

  @Test
  fun movieDetail_idNull_returnsZero() {
    val result = fullMovieDetail.copy(id = null).stubToMediaDetail()
    assertEquals(0, result.id)
  }

  // mediaKeywords branches: non-null, null, non-null with null keywords list

  @Test
  fun movieDetail_mediaKeywordsNonNull_returnsKeywords() {
    assertEquals(mediaKeywordsItems, validMovieDetail.keywords)
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
    assertNotNull(validMovieDetail.duration) // e.g. "2h 0m" depending on convertRuntime
  }

  @Test
  fun movieDetail_runtimeZero_returnsNullDuration() {
    val result = fullMovieDetail.copy(runtime = 0).stubToMediaDetail()
    assertNull(result.duration)
  }

  @Test
  fun movieDetail_runtimeNull_returnsNullDuration() {
    val result = fullMovieDetail.copy(runtime = null).stubToMediaDetail()
    assertNull(result.duration)
  }

  // budget branches: valid, zero, null

  @Test
  fun movieDetail_budgetValid_returnsFormattedUsd() {
    assertEquals("$1,000,000.00", validMovieDetail.budget)
  }

  @Test
  fun movieDetail_budgetZero_returnsDash() {
    val result = fullMovieDetail.copy(budget = 0).stubToMediaDetail()
    assertEquals("-", result.budget)
  }

  @Test
  fun movieDetail_budgetNull_returnsDash() {
    val result = fullMovieDetail.copy(budget = null).stubToMediaDetail()
    assertEquals("-", result.budget)
  }

  // revenue branches: valid, zero, null

  @Test
  fun movieDetail_revenueValid_returnsFormattedUsd() {
    assertEquals("$5,000,000.00", validMovieDetail.revenue)
  }

  @Test
  fun movieDetail_revenueZero_returnsDash() {
    val result = fullMovieDetail.copy(revenue = 0L).stubToMediaDetail()
    assertEquals("-", result.revenue)
  }

  @Test
  fun movieDetail_revenueNull_returnsDash() {
    val result = fullMovieDetail.copy(revenue = null).stubToMediaDetail()
    assertEquals("-", result.revenue)
  }

  // voteAverage branches: positive, zero, null

  @Test
  fun movieDetail_voteAveragePositive_returnsTmdbScore() {
    assertEquals("8.0", validMovieDetail.tmdbScore)
  }

  @Test
  fun movieDetail_voteAverageZero_returnsNullTmdbScore() {
    val result = fullMovieDetail.copy(voteAverage = 0.0).stubToMediaDetail()
    assertNull(result.tmdbScore)
  }

  @Test
  fun movieDetail_voteAverageNull_returnsNullTmdbScore() {
    val result = fullMovieDetail.copy(voteAverage = null).stubToMediaDetail()
    assertNull(result.tmdbScore)
  }

  // listGenres branches: valid list, null, empty, list with null item

  @Test
  fun movieDetail_genresValidList_returnsGenreString() {
    assertEquals("Action, Comedy", validMovieDetail.genre)
    assertEquals(listOf(1, 2), validMovieDetail.genreId)
  }

  @Test
  fun movieDetail_genresNull_returnsNullGenre() {
    val result = fullMovieDetail.copy(listGenres = null).stubToMediaDetail()
    assertNull(result.genre)
    assertNull(result.genreId)
  }

  @Test
  fun movieDetail_genresEmpty_returnsNullGenre() {
    val result = fullMovieDetail.copy(listGenres = emptyList()).stubToMediaDetail()
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

  // originalLanguage branches: known code, unknown code, null

  @Test
  fun movieDetail_languageKnownCode_returnsLanguageName() {
    val result = fullMovieDetail.copy(originalLanguage = "cs").stubToMediaDetail()
    assertEquals("Czech", result.language)
  }

  @Test
  fun movieDetail_languageUnknownCode_returnsEmpty() {
    val result = fullMovieDetail.copy(originalLanguage = "zz").stubToMediaDetail()
    assertEquals("", result.language)
  }

  @Test
  fun movieDetail_languageNull_returnsEmpty() {
    val result = fullMovieDetail.copy(originalLanguage = null)
      .stubToMediaDetail()
    assertEquals("", result.language)
  }

  // imdbId passthrough (movie reads directly from MovieDetail, not externalIds)

  @Test
  fun movieDetail_imdbIdNonNull_returnsImdbId() {
    assertEquals("tt9999999", validMovieDetail.imdbId)
  }

  @Test
  fun movieDetail_imdbIdNull_returnsNull() {
    val result = fullMovieDetail.copy(imdbId = null).stubToMediaDetail()
    assertNull(result.imdbId)
  }

  // status passthrough

  @Test
  fun movieDetail_statusNonNull_returnsStatus() {
    assertEquals("Released", validMovieDetail.status)
  }

  @Test
  fun movieDetail_statusNull_returnsNull() {
    val result = fullMovieDetail.copy(status = null).stubToMediaDetail()
    assertNull(result.status)
  }

  // total episodes and seasons

  @Test
  fun movieDetail_episodeAndSeasons_returnsNull() {
    assertEquals(null, validMovieDetail.totalSeasons)
    assertEquals(null, validMovieDetail.totalEpisodes)
  }

  // endregion

  private fun TvDetail.stubToMediaDetail() = toMediaDetail("US", mediaKeywords, tvExternalIds)
  private fun MovieDetail.stubToMediaDetail() = toMediaDetail(releaseDateRegion, mediaKeywords)
}
