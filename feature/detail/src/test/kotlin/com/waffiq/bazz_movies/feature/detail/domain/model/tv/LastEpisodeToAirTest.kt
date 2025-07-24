package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LastEpisodeToAirTest {

  @Test
  fun createLastEpisodeToAir_withValidValues_setsPropertiesCorrectly() {
    val lastEpisode = LastEpisodeToAir(
      productionCode = "PROD001",
      airDate = "2023-01-01",
      overview = "Episode overview",
      episodeNumber = 1,
      episodeType = "standard",
      showId = 123,
      voteAverage = 8.5,
      name = "Episode 1",
      seasonNumber = 1,
      runtime = 42,
      id = 456,
      stillPath = "/still.jpg",
      voteCount = 100
    )

    assertEquals("PROD001", lastEpisode.productionCode)
    assertEquals("2023-01-01", lastEpisode.airDate)
    assertEquals("Episode overview", lastEpisode.overview)
    assertEquals(1, lastEpisode.episodeNumber)
    assertEquals("standard", lastEpisode.episodeType)
    assertEquals(123, lastEpisode.showId)
    assertEquals(8.5, lastEpisode.voteAverage)
    assertEquals("Episode 1", lastEpisode.name)
    assertEquals(1, lastEpisode.seasonNumber)
    assertEquals(42, lastEpisode.runtime)
    assertEquals(456, lastEpisode.id)
    assertEquals("/still.jpg", lastEpisode.stillPath)
    assertEquals(100, lastEpisode.voteCount)
  }

  @Test
  fun createLastEpisodeToAir_withDefaultValues_setsAllPropertiesToNull() {
    val lastEpisode = LastEpisodeToAir()

    assertNull(lastEpisode.productionCode)
    assertNull(lastEpisode.airDate)
    assertNull(lastEpisode.overview)
    assertNull(lastEpisode.episodeNumber)
    assertNull(lastEpisode.episodeType)
    assertNull(lastEpisode.showId)
    assertNull(lastEpisode.voteAverage)
    assertNull(lastEpisode.name)
    assertNull(lastEpisode.seasonNumber)
    assertNull(lastEpisode.runtime)
    assertNull(lastEpisode.id)
    assertNull(lastEpisode.stillPath)
    assertNull(lastEpisode.voteCount)
  }

  @Test
  fun createLastEpisodeToAir_withPartialValues_setsSpecifiedPropertiesOnly() {
    val lastEpisode = LastEpisodeToAir(
      name = "Final Episode",
      episodeNumber = 10,
      voteAverage = 9.2
    )

    assertNull(lastEpisode.productionCode)
    assertNull(lastEpisode.airDate)
    assertNull(lastEpisode.overview)
    assertEquals(10, lastEpisode.episodeNumber)
    assertNull(lastEpisode.episodeType)
    assertNull(lastEpisode.showId)
    assertEquals(9.2, lastEpisode.voteAverage)
    assertEquals("Final Episode", lastEpisode.name)
    assertNull(lastEpisode.seasonNumber)
    assertNull(lastEpisode.runtime)
    assertNull(lastEpisode.id)
    assertNull(lastEpisode.stillPath)
    assertNull(lastEpisode.voteCount)
  }

  @Test
  fun createLastEpisodeToAir_withZeroValues_setsZeroValues() {
    val lastEpisode = LastEpisodeToAir(
      episodeNumber = 0,
      showId = 0,
      voteAverage = 0.0,
      seasonNumber = 0,
      runtime = 0,
      id = 0,
      voteCount = 0
    )

    assertEquals(0, lastEpisode.episodeNumber)
    assertEquals(0, lastEpisode.showId)
    assertEquals(0.0, lastEpisode.voteAverage)
    assertEquals(0, lastEpisode.seasonNumber)
    assertEquals(0, lastEpisode.runtime)
    assertEquals(0, lastEpisode.id)
    assertEquals(0, lastEpisode.voteCount)
  }

  @Test
  fun createLastEpisodeToAir_withEmptyStrings_setsEmptyStrings() {
    val lastEpisode = LastEpisodeToAir(
      productionCode = "",
      airDate = "",
      overview = "",
      episodeType = "",
      name = "",
      stillPath = ""
    )

    assertEquals("", lastEpisode.productionCode)
    assertEquals("", lastEpisode.airDate)
    assertEquals("", lastEpisode.overview)
    assertEquals("", lastEpisode.episodeType)
    assertEquals("", lastEpisode.name)
    assertEquals("", lastEpisode.stillPath)
  }

  @Test
  fun createLastEpisodeToAir_withNegativeValues_setsNegativeValues() {
    val lastEpisode = LastEpisodeToAir(
      episodeNumber = -1,
      showId = -10,
      voteAverage = -5.0,
      seasonNumber = -2,
      runtime = -30,
      id = -100,
      voteCount = -50
    )

    assertEquals(-1, lastEpisode.episodeNumber)
    assertEquals(-10, lastEpisode.showId)
    assertEquals(-5.0, lastEpisode.voteAverage)
    assertEquals(-2, lastEpisode.seasonNumber)
    assertEquals(-30, lastEpisode.runtime)
    assertEquals(-100, lastEpisode.id)
    assertEquals(-50, lastEpisode.voteCount)
  }
}
