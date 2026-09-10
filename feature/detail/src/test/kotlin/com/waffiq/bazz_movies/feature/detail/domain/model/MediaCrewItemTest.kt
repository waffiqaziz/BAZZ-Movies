package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaCrewItem
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MediaCrewItemTest {

  @Test
  fun createMediaCrewItem_withNoParameters_defaultNull() {
    val crewItem = MediaCrewItem()
    assertNull(crewItem.name)
    assertNull(crewItem.originalName)
    assertNull(crewItem.profilePath)
    assertNull(crewItem.id)
    assertNull(crewItem.creditId)
    assertNull(crewItem.totalEpisodeCount)
    assertNull(crewItem.gender)
    assertNull(crewItem.knownForDepartment)
    assertNull(crewItem.jobs)
    assertNull(crewItem.popularity)
    assertNull(crewItem.adult)
    assertNull(crewItem.department)
    assertNull(crewItem.job)
  }

  @Test
  fun createMediaCrewItem_withAllParameters_shouldNotNull() {
    assertNotNull(mediaCrewItem.name)
    assertNotNull(mediaCrewItem.originalName)
    assertNotNull(mediaCrewItem.profilePath)
    assertNotNull(mediaCrewItem.id)
    assertNotNull(mediaCrewItem.creditId)
    assertNotNull(mediaCrewItem.totalEpisodeCount)
    assertNotNull(mediaCrewItem.gender)
    assertNotNull(mediaCrewItem.knownForDepartment)
    assertNotNull(mediaCrewItem.jobs)
    assertNotNull(mediaCrewItem.popularity)
    assertNotNull(mediaCrewItem.adult)
    assertNotNull(mediaCrewItem.department)
    assertNotNull(mediaCrewItem.job)
  }
}
