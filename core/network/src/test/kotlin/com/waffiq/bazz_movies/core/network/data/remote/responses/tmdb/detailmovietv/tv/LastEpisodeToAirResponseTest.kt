package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.lastEpisodeToAirResponse1
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class LastEpisodeToAirResponseTest {

  @Test
  fun lastEpisodeToAirResponse_withValidValues_setsPropertiesCorrectly() {
    val lastEpisodeToAirResponse = lastEpisodeToAirResponse1
    assertEquals(5732896, lastEpisodeToAirResponse.id)
    assertEquals("Episode 12", lastEpisodeToAirResponse.name)
    assertEquals(
      """
        Sa-eon abandons everything he's worked for, leaving Hee-joo completely alone. But after a clue 
        arrives, she embarks on a journey to reunite with him.
      """.trimIndent(),
      lastEpisodeToAirResponse.overview
    )
    assertEquals(0.0, lastEpisodeToAirResponse.voteAverage)
    assertEquals(0, lastEpisodeToAirResponse.voteCount)
    assertEquals("2025-01-04", lastEpisodeToAirResponse.airDate)
    assertEquals(12, lastEpisodeToAirResponse.episodeNumber)
    assertEquals("finale", lastEpisodeToAirResponse.episodeType)
    assertEquals("", lastEpisodeToAirResponse.productionCode)
    assertEquals(67, lastEpisodeToAirResponse.runtime)
    assertEquals(1, lastEpisodeToAirResponse.seasonNumber)
    assertEquals(253905, lastEpisodeToAirResponse.showId)
    assertEquals("/9gvIN5sjFV3EbAasl1nhfMqwv8Z.jpg", lastEpisodeToAirResponse.stillPath)
  }

  @Test
  fun lastEpisodeToAirResponse_withDefaultValues_setsPropertiesCorrectly() {
    val lastEpisodeToAirResponse = LastEpisodeToAirResponse()
    assertNull(lastEpisodeToAirResponse.productionCode)
    assertNull(lastEpisodeToAirResponse.airDate)
    assertNull(lastEpisodeToAirResponse.overview)
    assertNull(lastEpisodeToAirResponse.episodeNumber)
    assertNull(lastEpisodeToAirResponse.showId)
    assertNull(lastEpisodeToAirResponse.voteAverage)
    assertNull(lastEpisodeToAirResponse.name)
    assertNull(lastEpisodeToAirResponse.seasonNumber)
    assertNull(lastEpisodeToAirResponse.episodeType)
    assertNull(lastEpisodeToAirResponse.runtime)
    assertNull(lastEpisodeToAirResponse.id)
    assertNull(lastEpisodeToAirResponse.stillPath)
    assertNull(lastEpisodeToAirResponse.voteCount)
  }

  @Test
  fun lastEpisodeToAirResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val lastEpisodeToAirResponse = LastEpisodeToAirResponse(
      name = "Episode 12",
      id = 2
    )
    assertEquals("Episode 12", lastEpisodeToAirResponse.name)
    assertNull(lastEpisodeToAirResponse.voteCount)
  }
}
