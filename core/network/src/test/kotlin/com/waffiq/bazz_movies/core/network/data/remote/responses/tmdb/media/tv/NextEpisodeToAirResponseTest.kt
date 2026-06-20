package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.nextEpisodeToAirResponse
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Test

class NextEpisodeToAirResponseTest {

  @Test
  fun nextEpisodeToAirResponse_withValidValues_setsPropertiesCorrectly() {
    val result = nextEpisodeToAirResponse
    Assert.assertEquals(67884, result.id)
    Assert.assertEquals("name episode", result.name)
    Assert.assertEquals("2026-06-20", result.airDate)
  }

  @Test
  fun nextEpisodeToAirResponse_withDefaultValues_setsAllPropertiesToNull() {
    val result = NextEpisodeToAirResponse()
    assertNull(result.id)
    assertNull(result.name)
    assertNull(result.airDate)
  }
}
