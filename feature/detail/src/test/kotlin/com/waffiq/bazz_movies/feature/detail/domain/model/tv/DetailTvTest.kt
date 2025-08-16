package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.tvDetailFull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DetailTvTest {

  @Test
  fun createDetailTv_withValidValues_setsPropertiesCorrectly() {
    val tvDetail = tvDetailFull

    assertEquals("en", tvDetail.originalLanguage)
    assertEquals(62, tvDetail.numberOfEpisodes)
    assertEquals(1, tvDetail.listNetworksItem?.size)
    assertEquals("Scripted", tvDetail.type)
    assertEquals("/backdrop.jpg", tvDetail.backdropPath)
    assertEquals(1, tvDetail.listGenres?.size)
    assertEquals(100.5, tvDetail.popularity)
    assertEquals(1396, tvDetail.id)
    assertEquals(5, tvDetail.numberOfSeasons)
    assertEquals(5000, tvDetail.voteCount)
    assertEquals("2008-01-20", tvDetail.firstAirDate)
    assertEquals("Show overview", tvDetail.overview)
    assertEquals(1, tvDetail.listSeasonsItem?.size)
    assertEquals(1, tvDetail.listLanguages?.size)
    assertEquals(1, tvDetail.listCreatedByItem?.size)
    assertNotNull(tvDetail.lastEpisodeToAir)
    assertEquals("/poster.jpg", tvDetail.posterPath)
    assertEquals(1, tvDetail.listOriginCountry?.size)
    assertEquals("Breaking Bad", tvDetail.originalName)
    assertEquals(9.3, tvDetail.voteAverage)
    assertEquals("Breaking Bad", tvDetail.name)
    assertEquals("All Bad Things Must Come to an End", tvDetail.tagline)
    assertEquals(1, tvDetail.listEpisodeRunTime?.size)
    assertNotNull(tvDetail.contentRatings)
    assertEquals(false, tvDetail.adult)
    assertEquals("Episode data", tvDetail.nextEpisodeToAir)
    assertEquals(false, tvDetail.inProduction)
    assertEquals("2013-09-29", tvDetail.lastAirDate)
    assertEquals("http://www.amc.com/shows/breaking-bad", tvDetail.homepage)
    assertEquals("Ended", tvDetail.status)
  }

  @Test
  fun createDetailTv_withDefaultValues_setsAllPropertiesToNull() {
    val detailTv = TvDetail()

    assertNull(detailTv.originalLanguage)
    assertNull(detailTv.numberOfEpisodes)
    assertNull(detailTv.listNetworksItem)
    assertNull(detailTv.type)
    assertNull(detailTv.backdropPath)
    assertNull(detailTv.listGenres)
    assertNull(detailTv.popularity)
    assertNull(detailTv.listProductionCountriesItem)
    assertNull(detailTv.id)
    assertNull(detailTv.numberOfSeasons)
    assertNull(detailTv.voteCount)
    assertNull(detailTv.firstAirDate)
    assertNull(detailTv.overview)
    assertNull(detailTv.listSeasonsItem)
    assertNull(detailTv.listLanguages)
    assertNull(detailTv.listCreatedByItem)
    assertNull(detailTv.lastEpisodeToAir)
    assertNull(detailTv.posterPath)
    assertNull(detailTv.listOriginCountry)
    assertNull(detailTv.listSpokenLanguagesItem)
    assertNull(detailTv.listProductionCompaniesItem)
    assertNull(detailTv.originalName)
    assertNull(detailTv.voteAverage)
    assertNull(detailTv.name)
    assertNull(detailTv.tagline)
    assertNull(detailTv.listEpisodeRunTime)
    assertNull(detailTv.contentRatings)
    assertNull(detailTv.adult)
    assertNull(detailTv.nextEpisodeToAir)
    assertNull(detailTv.inProduction)
    assertNull(detailTv.lastAirDate)
    assertNull(detailTv.homepage)
    assertNull(detailTv.status)
  }

  @Test
  fun createDetailTv_withPartialValues_setsSpecifiedPropertiesOnly() {
    val detailTv = TvDetail(
      name = "Game of Thrones",
      id = 1399,
      adult = true,
      inProduction = true
    )

    assertEquals("Game of Thrones", detailTv.name)
    assertEquals(1399, detailTv.id)
    assertEquals(true, detailTv.adult)
    assertEquals(true, detailTv.inProduction)
    assertNull(detailTv.originalLanguage)
    assertNull(detailTv.numberOfEpisodes)
    assertNull(detailTv.overview)
  }

  @Test
  fun createDetailTv_withZeroValues_setsZeroValues() {
    val detailTv = TvDetail(
      numberOfEpisodes = 0,
      popularity = 0.0,
      id = 0,
      numberOfSeasons = 0,
      voteCount = 0,
      voteAverage = 0.0
    )

    assertEquals(0, detailTv.numberOfEpisodes)
    assertEquals(0.0, detailTv.popularity)
    assertEquals(0, detailTv.id)
    assertEquals(0, detailTv.numberOfSeasons)
    assertEquals(0, detailTv.voteCount)
    assertEquals(0.0, detailTv.voteAverage)
  }

  @Test
  fun createDetailTv_withEmptyLists_setsEmptyLists() {
    val detailTv = TvDetail(
      listNetworksItem = emptyList(),
      listGenres = emptyList(),
      listLanguages = emptyList(),
      listOriginCountry = emptyList(),
      listEpisodeRunTime = emptyList()
    )

    assertTrue(detailTv.listNetworksItem?.isEmpty() == true)
    assertTrue(detailTv.listGenres?.isEmpty() == true)
    assertTrue(detailTv.listLanguages?.isEmpty() == true)
    assertTrue(detailTv.listOriginCountry?.isEmpty() == true)
    assertTrue(detailTv.listEpisodeRunTime?.isEmpty() == true)
  }

  @Test
  fun createDetailTv_withNullListItems_setsNullListItems() {
    val detailTv = TvDetail(
      listNetworksItem = listOf(null, null),
      listLanguages = listOf(null, "en", null),
      listEpisodeRunTime = listOf(null, 45, null)
    )

    assertEquals(2, detailTv.listNetworksItem?.size)
    assertNull(detailTv.listNetworksItem?.get(0))
    assertNull(detailTv.listNetworksItem?.get(1))

    assertEquals(3, detailTv.listLanguages?.size)
    assertNull(detailTv.listLanguages?.get(0))
    assertEquals("en", detailTv.listLanguages?.get(1))
    assertNull(detailTv.listLanguages?.get(2))

    assertEquals(3, detailTv.listEpisodeRunTime?.size)
    assertNull(detailTv.listEpisodeRunTime?.get(0))
    assertEquals(45, detailTv.listEpisodeRunTime?.get(1))
    assertNull(detailTv.listEpisodeRunTime?.get(2))
  }

  @Test
  fun createDetailTv_withEmptyStrings_setsEmptyStrings() {
    val detailTv = TvDetail(
      originalLanguage = "",
      type = "",
      backdropPath = "",
      name = "",
      tagline = "",
      homepage = "",
      status = ""
    )

    assertEquals("", detailTv.originalLanguage)
    assertEquals("", detailTv.type)
    assertEquals("", detailTv.backdropPath)
    assertEquals("", detailTv.name)
    assertEquals("", detailTv.tagline)
    assertEquals("", detailTv.homepage)
    assertEquals("", detailTv.status)
  }

  @Test
  fun createDetailTv_withNegativeValues_setsNegativeValues() {
    val detailTv = TvDetail(
      numberOfEpisodes = -1,
      popularity = -5.0,
      id = -100,
      numberOfSeasons = -2,
      voteCount = -50,
      voteAverage = -1.5
    )

    assertEquals(-1, detailTv.numberOfEpisodes)
    assertEquals(-5.0, detailTv.popularity)
    assertEquals(-100, detailTv.id)
    assertEquals(-2, detailTv.numberOfSeasons)
    assertEquals(-50, detailTv.voteCount)
    assertEquals(-1.5, detailTv.voteAverage)
  }
}
