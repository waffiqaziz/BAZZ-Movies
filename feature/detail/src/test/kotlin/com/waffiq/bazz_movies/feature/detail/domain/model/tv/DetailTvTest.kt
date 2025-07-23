package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.core.domain.GenresItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCompaniesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.ProductionCountriesItem
import com.waffiq.bazz_movies.feature.detail.domain.model.SpokenLanguagesItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DetailTvTest {

  @Test
  fun createDetailTv_withValidValues_setsPropertiesCorrectly() {
    val detailTv = DetailTv(
      originalLanguage = "en",
      numberOfEpisodes = 62,
      listNetworksItem = listOf(NetworksItem(name = "AMC")),
      type = "Scripted",
      backdropPath = "/backdrop.jpg",
      listGenres = listOf(GenresItem()),
      popularity = 100.5,
      listProductionCountriesItem = listOf(ProductionCountriesItem()),
      id = 1396,
      numberOfSeasons = 5,
      voteCount = 5000,
      firstAirDate = "2008-01-20",
      overview = "Show overview",
      listSeasonsItem = listOf(SeasonsItem()),
      listLanguages = listOf("en"),
      listCreatedByItem = listOf(CreatedByItem()),
      lastEpisodeToAir = LastEpisodeToAir(),
      posterPath = "/poster.jpg",
      listOriginCountry = listOf("US"),
      listSpokenLanguagesItem = listOf(SpokenLanguagesItem()),
      listProductionCompaniesItem = listOf(ProductionCompaniesItem()),
      originalName = "Breaking Bad",
      voteAverage = 9.3,
      name = "Breaking Bad",
      tagline = "All Bad Things Must Come to an End",
      listEpisodeRunTime = listOf(47),
      contentRatings = ContentRatings(),
      adult = false,
      nextEpisodeToAir = "Episode data",
      inProduction = false,
      lastAirDate = "2013-09-29",
      homepage = "http://www.amc.com/shows/breaking-bad",
      status = "Ended"
    )

    assertEquals("en", detailTv.originalLanguage)
    assertEquals(62, detailTv.numberOfEpisodes)
    assertEquals(1, detailTv.listNetworksItem?.size)
    assertEquals("Scripted", detailTv.type)
    assertEquals("/backdrop.jpg", detailTv.backdropPath)
    assertEquals(1, detailTv.listGenres?.size)
    assertEquals(100.5, detailTv.popularity)
    assertEquals(1396, detailTv.id)
    assertEquals(5, detailTv.numberOfSeasons)
    assertEquals(5000, detailTv.voteCount)
    assertEquals("2008-01-20", detailTv.firstAirDate)
    assertEquals("Show overview", detailTv.overview)
    assertEquals(1, detailTv.listSeasonsItem?.size)
    assertEquals(1, detailTv.listLanguages?.size)
    assertEquals(1, detailTv.listCreatedByItem?.size)
    assertNotNull(detailTv.lastEpisodeToAir)
    assertEquals("/poster.jpg", detailTv.posterPath)
    assertEquals(1, detailTv.listOriginCountry?.size)
    assertEquals("Breaking Bad", detailTv.originalName)
    assertEquals(9.3, detailTv.voteAverage)
    assertEquals("Breaking Bad", detailTv.name)
    assertEquals("All Bad Things Must Come to an End", detailTv.tagline)
    assertEquals(1, detailTv.listEpisodeRunTime?.size)
    assertNotNull(detailTv.contentRatings)
    assertEquals(false, detailTv.adult)
    assertEquals("Episode data", detailTv.nextEpisodeToAir)
    assertEquals(false, detailTv.inProduction)
    assertEquals("2013-09-29", detailTv.lastAirDate)
    assertEquals("http://www.amc.com/shows/breaking-bad", detailTv.homepage)
    assertEquals("Ended", detailTv.status)
  }

  @Test
  fun createDetailTv_withDefaultValues_setsAllPropertiesToNull() {
    val detailTv = DetailTv()

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
    val detailTv = DetailTv(
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
    val detailTv = DetailTv(
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
    val detailTv = DetailTv(
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
    val detailTv = DetailTv(
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
    val detailTv = DetailTv(
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
    val detailTv = DetailTv(
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
