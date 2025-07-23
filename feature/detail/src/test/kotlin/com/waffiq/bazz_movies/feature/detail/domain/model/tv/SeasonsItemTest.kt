package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SeasonsItemTest {

  @Test
  fun createSeasonsItem_withValidValues_setsPropertiesCorrectly() {
    val seasonsItem = SeasonsItem(
      airDate = "2023-01-01",
      overview = "Season overview",
      episodeCount = 10,
      name = "Season 1",
      seasonNumber = 1,
      id = 123,
      posterPath = "/poster.jpg"
    )

    assertEquals("2023-01-01", seasonsItem.airDate)
    assertEquals("Season overview", seasonsItem.overview)
    assertEquals(10, seasonsItem.episodeCount)
    assertEquals("Season 1", seasonsItem.name)
    assertEquals(1, seasonsItem.seasonNumber)
    assertEquals(123, seasonsItem.id)
    assertEquals("/poster.jpg", seasonsItem.posterPath)
  }

  @Test
  fun createSeasonsItem_withDefaultValues_setsAllPropertiesToNull() {
    val seasonsItem = SeasonsItem()

    assertNull(seasonsItem.airDate)
    assertNull(seasonsItem.overview)
    assertNull(seasonsItem.episodeCount)
    assertNull(seasonsItem.name)
    assertNull(seasonsItem.seasonNumber)
    assertNull(seasonsItem.id)
    assertNull(seasonsItem.posterPath)
  }

  @Test
  fun createSeasonsItem_withPartialValues_setsSpecifiedPropertiesOnly() {
    val seasonsItem = SeasonsItem(
      name = "Season 2",
      seasonNumber = 2,
      episodeCount = 8
    )

    assertNull(seasonsItem.airDate)
    assertNull(seasonsItem.overview)
    assertEquals(8, seasonsItem.episodeCount)
    assertEquals("Season 2", seasonsItem.name)
    assertEquals(2, seasonsItem.seasonNumber)
    assertNull(seasonsItem.id)
    assertNull(seasonsItem.posterPath)
  }

  @Test
  fun createSeasonsItem_withZeroValues_setsZeroValues() {
    val seasonsItem = SeasonsItem(
      episodeCount = 0,
      seasonNumber = 0,
      id = 0
    )

    assertEquals(0, seasonsItem.episodeCount)
    assertEquals(0, seasonsItem.seasonNumber)
    assertEquals(0, seasonsItem.id)
  }

  @Test
  fun createSeasonsItem_withEmptyStrings_setsEmptyStrings() {
    val seasonsItem = SeasonsItem(
      airDate = "",
      overview = "",
      name = "",
      posterPath = ""
    )

    assertEquals("", seasonsItem.airDate)
    assertEquals("", seasonsItem.overview)
    assertEquals("", seasonsItem.name)
    assertEquals("", seasonsItem.posterPath)
  }

  @Test
  fun createSeasonsItem_withNegativeValues_setsNegativeValues() {
    val seasonsItem = SeasonsItem(
      episodeCount = -1,
      seasonNumber = -5,
      id = -10
    )

    assertEquals(-1, seasonsItem.episodeCount)
    assertEquals(-5, seasonsItem.seasonNumber)
    assertEquals(-10, seasonsItem.id)
  }
}
