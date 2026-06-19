package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.NextEpisodeToAirResponse
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.nextEpisodeToAirResponse
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toNextEpisodeToAir
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NextEpisodeToAirMapperTest {

  @Test
  fun toNextEpisodeToAir_withValidValues_returnsNextEpisodeToAir() {
    val result = nextEpisodeToAirResponse.toNextEpisodeToAir()
    assertEquals(674, result.id)
    assertEquals("name", result.name)
    assertEquals("2026-06-19", result.airDate)
  }

  @Test
  fun toNextEpisodeToAir_withNullValues_returnsNull() {
    val result = NextEpisodeToAirResponse().toNextEpisodeToAir()
    assertNull(result.id)
    assertNull(result.name)
    assertNull(result.airDate)
  }
}
