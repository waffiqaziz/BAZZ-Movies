package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.seasonsResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SeasonsResponseItemTest {

  @Test
  fun seasonsResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals("2024-11-22", seasonsResponseItem.airDate)
    assertEquals("Overview", seasonsResponseItem.overview)
    assertEquals(12, seasonsResponseItem.episodeCount)
    assertEquals("When the Phone Rings", seasonsResponseItem.name)
    assertEquals(1, seasonsResponseItem.seasonNumber)
    assertEquals(392789, seasonsResponseItem.id)
    assertEquals("/glWP5Y7CVeqrOjJpLckQjuLFjQJ.jpg", seasonsResponseItem.posterPath)
  }

  @Test
  fun seasonsResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val seasonsResponseItem = SeasonsResponseItem()
    assertNull(seasonsResponseItem.airDate)
    assertNull(seasonsResponseItem.overview)
    assertNull(seasonsResponseItem.episodeCount)
    assertNull(seasonsResponseItem.name)
    assertNull(seasonsResponseItem.seasonNumber)
    assertNull(seasonsResponseItem.id)
    assertNull(seasonsResponseItem.posterPath)
  }

  @Test
  fun seasonsResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val seasonsResponseItem = SeasonsResponseItem(
      id = 659874,
    )
    assertEquals(659874, seasonsResponseItem.id)
    assertNull(seasonsResponseItem.name)
  }
}
