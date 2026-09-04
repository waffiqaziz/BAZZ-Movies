package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.seasonsResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SeasonsResponseItemTest {

  @Test
  fun seasonsItemResponse_withValidValues_setsPropertiesCorrectly() {
    val seasonsItemResponse = seasonsResponseItem
    assertEquals("2024-11-22", seasonsItemResponse.airDate)
    assertEquals("Overview", seasonsItemResponse.overview)
    assertEquals(12, seasonsItemResponse.episodeCount)
    assertEquals("When the Phone Rings", seasonsItemResponse.name)
    assertEquals(1, seasonsItemResponse.seasonNumber)
    assertEquals(392789, seasonsItemResponse.id)
    assertEquals("/glWP5Y7CVeqrOjJpLckQjuLFjQJ.jpg", seasonsItemResponse.posterPath)
  }

  @Test
  fun seasonsItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val seasonsItemResponse = SeasonsResponseItem()
    assertNull(seasonsItemResponse.airDate)
    assertNull(seasonsItemResponse.overview)
    assertNull(seasonsItemResponse.episodeCount)
    assertNull(seasonsItemResponse.name)
    assertNull(seasonsItemResponse.seasonNumber)
    assertNull(seasonsItemResponse.id)
    assertNull(seasonsItemResponse.posterPath)
  }

  @Test
  fun seasonsItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val seasonsItemResponse = SeasonsResponseItem(
      id = 659874,
    )
    assertEquals(659874, seasonsItemResponse.id)
    assertNull(seasonsItemResponse.name)
  }
}
