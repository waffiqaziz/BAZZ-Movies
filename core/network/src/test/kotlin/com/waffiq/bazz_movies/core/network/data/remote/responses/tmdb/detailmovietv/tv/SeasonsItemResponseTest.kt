package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.seasonsItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class SeasonsItemResponseTest {

  @Test
  fun seasonsItemResponse_withValidValues_setsPropertiesCorrectly() {
    val seasonsItemResponse = seasonsItemResponseDump
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
    val seasonsItemResponse = SeasonsItemResponse()
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
    val seasonsItemResponse = SeasonsItemResponse(
      id = 659874,
    )
    assertEquals(659874, seasonsItemResponse.id)
    assertNull(seasonsItemResponse.name)
  }
}
