package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.tvCrewResponseItem1
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TvCrewResponseItemTest {

  @Test
  fun tvCrewResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertNotNull(tvCrewResponseItem1.totalEpisodeCount)
    assertNotNull(tvCrewResponseItem1.gender)
    assertNotNull(tvCrewResponseItem1.knownForDepartment)
    assertNotNull(tvCrewResponseItem1.originalName)
    assertNotNull(tvCrewResponseItem1.popularity)
    assertNotNull(tvCrewResponseItem1.jobs)
    assertNotNull(tvCrewResponseItem1.name)
    assertNotNull(tvCrewResponseItem1.profilePath)
    assertNotNull(tvCrewResponseItem1.id)
    assertNotNull(tvCrewResponseItem1.adult)
    assertNotNull(tvCrewResponseItem1.department)
  }

  @Test
  fun tvCrewResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val nullData = TvCrewResponseItem()
    assertNull(nullData.totalEpisodeCount)
    assertNull(nullData.gender)
    assertNull(nullData.knownForDepartment)
    assertNull(nullData.originalName)
    assertNull(nullData.popularity)
    assertNull(nullData.jobs)
    assertNull(nullData.name)
    assertNull(nullData.profilePath)
    assertNull(nullData.id)
    assertNull(nullData.adult)
    assertNull(nullData.department)
  }
}
