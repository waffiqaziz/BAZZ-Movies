package com.waffiq.bazz_movies.feature.detail.domain.model.releasedate

import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseDateRegionTest {

  @Test
  fun createReleaseDateRegion_withValidValues_shouldSetPropertiesCorrectly() {
    val regionRelease = "US"
    val releaseDate = "2024-01-01"

    val item = ReleaseDateRegion(
      regionRelease = regionRelease,
      releaseDate = releaseDate
    )

    assertEquals(regionRelease, item.regionRelease)
    assertEquals(releaseDate, item.releaseDate)
  }
}
