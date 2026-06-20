package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.nextEpisodeToAir
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Test

class NextEpisodeToAirTest {

  @Test
  fun nextEpisodeToAir_withValidValues_setsPropertiesCorrectly() {
    val result = nextEpisodeToAir
    Assert.assertEquals(674, result.id)
    Assert.assertEquals("name", result.name)
    Assert.assertEquals("2026-06-19", result.airDate)
  }

  @Test
  fun nextEpisodeToAir_withDefaultValues_setsAllPropertiesToNull() {
    val result = NextEpisodeToAir()
    assertNull(result.id)
    assertNull(result.name)
    assertNull(result.airDate)
  }
}
