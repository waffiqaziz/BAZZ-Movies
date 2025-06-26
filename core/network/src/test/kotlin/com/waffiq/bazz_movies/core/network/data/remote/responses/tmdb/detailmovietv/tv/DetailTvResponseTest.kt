package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.GenresItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.ProductionCountriesItemResponse
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.detailTvResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DetailTvResponseTest {

  val detailTvResponse = detailTvResponseDump

  @Test
  fun detailTvResponse_withValidValues_setsBasicInfoCorrectly() {
    assertEquals(253905, detailTvResponse.id)
    assertEquals("Miniseries", detailTvResponse.type)
    assertEquals("When the Phone Rings", detailTvResponse.name)
    assertEquals("Drama", detailTvResponse.genres?.get(0)?.name)
    assertEquals(394.215, detailTvResponse.popularity)
    assertEquals(8.4, detailTvResponse.voteAverage)
    assertEquals(141, detailTvResponse.voteCount)
    assertEquals("지금 거신 전화는", detailTvResponse.originalName)
    assertEquals("Korean", detailTvResponse.spokenLanguagesResponse?.get(0)?.englishName)
    assertEquals("ko", detailTvResponse.originalLanguage)
    assertEquals("ko", detailTvResponse.languages?.get(0))
    assertEquals("Ended", detailTvResponse.status)
    assertEquals("https://program.imbc.com/WhenThePhoneRings", detailTvResponse.homepage)
    assertEquals("Kim Ji-woon", detailTvResponse.createdByResponse?.get(0)?.name)
    assertTrue(detailTvResponse.adult == false)
  }

  @Test
  fun detailTvResponse_withValidValues_setsDatesCorrectly() {
    assertEquals("2024-11-22", detailTvResponse.firstAirDate)
    assertEquals("2025-01-04", detailTvResponse.lastAirDate)
    assertNull(detailTvResponse.nextEpisodeToAir)
  }

  @Test
  fun detailTvResponse_withValidValues_setsMediaAssetsCorrectly() {
    assertEquals("/2vtI9xzD6qpDzY9m8kV67QY0qfM.jpg", detailTvResponse.backdropPath)
    assertEquals("/glWP5Y7CVeqrOjJpLckQjuLFjQJ.jpg", detailTvResponse.posterPath)
    assertEquals("/pOSCKaZhndUFYtxHXjQOV6xJi1s.png", detailTvResponse.networksResponse?.get(0)?.logoPath)
  }

  @Test
  fun detailTvResponse_withValidValues_setsSeasonsAndEpisodesCorrectly() {
    assertEquals(1, detailTvResponse.numberOfSeasons)
    assertEquals(12, detailTvResponse.numberOfEpisodes)
    assertEquals(392789, detailTvResponse.seasonsResponse?.get(0)?.id)
    assertEquals(70, detailTvResponse.episodeRunTime?.get(0))
    assertEquals(67, detailTvResponse.lastEpisodeToAirResponse?.runtime)
  }

  @Test
  fun detailTvResponse_withValidValues_setsProductionAndCountryCorrectly() {
    assertEquals("South Korea", detailTvResponse.productionCountriesResponse?.get(0)?.name)
    assertEquals("Baram Pictures", detailTvResponse.productionCompaniesResponse?.get(1)?.name)
    assertEquals("KR", detailTvResponse.originCountry?.get(0))
    assertEquals(
      "KR",
      detailTvResponse.contentRatingsResponse?.contentRatingsItemResponse?.get(1)?.iso31661
    )
  }

  @Test
  fun detailTvResponse_withValidValues_setsOthersPropertiesCorrectly() {
    assertEquals(
      """
        A rising politician and his mute wife's tense marriage begins to unravel after a call from a 
        kidnapper turns their lives upside down.
      """.trimIndent(),
      detailTvResponse.overview
    )
    assertEquals(
      "Their love hangs by a thread... until a stranger picks up the phone.",
      detailTvResponse.tagline
    )
    assertTrue(detailTvResponse.inProduction == false)
  }

  @Test
  fun detailTvResponse_withDefaultValues_setsPropertiesCorrectly() {
    val detailTvResponse = DetailTvResponse()
    assertNull(detailTvResponse.originalLanguage)
    assertNull(detailTvResponse.numberOfEpisodes)
    assertNull(detailTvResponse.networksResponse)
    assertNull(detailTvResponse.type)
    assertNull(detailTvResponse.backdropPath)
    assertNull(detailTvResponse.genres)
    assertNull(detailTvResponse.popularity)
    assertNull(detailTvResponse.productionCountriesResponse)
    assertNull(detailTvResponse.id)
    assertNull(detailTvResponse.numberOfSeasons)
    assertNull(detailTvResponse.voteCount)
    assertNull(detailTvResponse.firstAirDate)
    assertNull(detailTvResponse.overview)
    assertNull(detailTvResponse.seasonsResponse)
    assertNull(detailTvResponse.languages)
    assertNull(detailTvResponse.createdByResponse)
    assertNull(detailTvResponse.lastEpisodeToAirResponse)
    assertNull(detailTvResponse.posterPath)
    assertNull(detailTvResponse.originCountry)
    assertNull(detailTvResponse.spokenLanguagesResponse)
    assertNull(detailTvResponse.productionCompaniesResponse)
    assertNull(detailTvResponse.originalName)
    assertNull(detailTvResponse.voteAverage)
    assertNull(detailTvResponse.name)
    assertNull(detailTvResponse.tagline)
    assertNull(detailTvResponse.episodeRunTime)
    assertNull(detailTvResponse.contentRatingsResponse)
    assertNull(detailTvResponse.adult)
    assertNull(detailTvResponse.nextEpisodeToAir)
    assertNull(detailTvResponse.inProduction)
    assertNull(detailTvResponse.lastAirDate)
    assertNull(detailTvResponse.homepage)
    assertNull(detailTvResponse.status)
  }

  @Test
  fun detailTvResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val detailTvResponse = DetailTvResponse(
      originalLanguage = "Bahasa",
      numberOfEpisodes = 1,
      networksResponse = listOf(NetworksItemResponse()),
      type = "tv",
      backdropPath = "backdrop_path",
      genres = listOf(GenresItemResponse()),
      popularity = 11111.0,
      productionCountriesResponse = listOf(ProductionCountriesItemResponse()),
      id = 1,
      numberOfSeasons = 1,
      voteCount = 1,
      firstAirDate = "first_air_data",
      overview = "overview",
      seasonsResponse = listOf(SeasonsItemResponse()),
      languages = listOf("id"),
      createdByResponse = listOf(CreatedByItemResponse()),
      posterPath = "poster_path",
      originCountry = listOf("id"),
      spokenLanguagesResponse = listOf(SpokenLanguagesItemResponse()),
      productionCompaniesResponse = listOf(ProductionCompaniesItemResponse()),
      originalName = "original_name",
      voteAverage = 1234.0,
      name = "name",
      tagline = "tag_line",
      episodeRunTime = listOf(123),
      contentRatingsResponse = ContentRatingsResponse(),
      adult = false,
      nextEpisodeToAir = "next_episode_to_air",
      inProduction = false,
      lastAirDate = "last_air_date",
      homepage = "home_page",
      status = "status"

    )
    assertNull(detailTvResponse.productionCountriesResponse?.get(0)?.name)

    DetailTvResponse(lastEpisodeToAirResponse = LastEpisodeToAirResponse())
  }
}
