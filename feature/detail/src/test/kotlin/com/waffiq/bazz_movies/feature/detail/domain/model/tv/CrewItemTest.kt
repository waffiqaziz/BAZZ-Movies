package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.crewItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.jobsItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CrewItemTest {

  @Test
  fun castItem_withValidValues_getsPropertiesCorrectly() {
    assertEquals(224, crewItem.id)
    assertEquals("name crew", crewItem.name)
    assertEquals(listOf(jobsItem), crewItem.jobs)
    assertEquals(12, crewItem.totalEpisodeCount)
    assertEquals(0, crewItem.gender)
    assertEquals("writter", crewItem.knownForDepartment)
    assertEquals("name crew original", crewItem.originalName)
    assertEquals(3232, crewItem.popularity)
    assertEquals("path.jpg", crewItem.profilePath)
    assertEquals(false, crewItem.adult)
    assertEquals("writter", crewItem.department)
  }

  @Test
  fun castItem_withDefaultValues_getsPropertiesCorrectly() {
    val crewItem = CrewItem()
    assertNull(crewItem.totalEpisodeCount)
    assertNull(crewItem.gender)
    assertNull(crewItem.knownForDepartment)
    assertNull(crewItem.originalName)
    assertNull(crewItem.popularity)
    assertNull(crewItem.jobs)
    assertNull(crewItem.name)
    assertNull(crewItem.profilePath)
    assertNull(crewItem.id)
    assertNull(crewItem.adult)
    assertNull(crewItem.department)
  }
}
