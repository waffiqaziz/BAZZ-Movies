package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DummyData.mediaCrewResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaCrewResponseItemTest {

  @Test
  fun mediaCrewResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals(0, mediaCrewResponseItem.gender)
    assertEquals("64fc09ebf85958011ca070b4", mediaCrewResponseItem.creditId)
    assertEquals("Visual Effects", mediaCrewResponseItem.knownForDepartment)
    assertEquals("Frank Schlegel", mediaCrewResponseItem.originalName)
    assertEquals(0.001, mediaCrewResponseItem.popularity)
    assertEquals("Frank Schlegel", mediaCrewResponseItem.name)
    assertEquals(null, mediaCrewResponseItem.profilePath)
    assertEquals(3014542, mediaCrewResponseItem.id)
    assertEquals(false, mediaCrewResponseItem.adult)
    assertEquals("Visual Effects", mediaCrewResponseItem.department)
    assertEquals("VFX Supervisor", mediaCrewResponseItem.job)
  }

  @Test
  fun mediaCrewResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val mediaCrewResponseItemNull = MediaCrewResponseItem()
    assertNull(mediaCrewResponseItemNull.gender)
    assertNull(mediaCrewResponseItemNull.creditId)
    assertNull(mediaCrewResponseItemNull.knownForDepartment)
    assertNull(mediaCrewResponseItemNull.originalName)
    assertNull(mediaCrewResponseItemNull.popularity)
    assertNull(mediaCrewResponseItemNull.name)
    assertNull(mediaCrewResponseItemNull.profilePath)
    assertNull(mediaCrewResponseItemNull.id)
    assertNull(mediaCrewResponseItemNull.adult)
    assertNull(mediaCrewResponseItemNull.department)
    assertNull(mediaCrewResponseItemNull.job)
  }

  @Test
  fun mediaCrewResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val mediaCrewResponseItem = MediaCrewResponseItem(
      name = "Martin Freeman",
      id = 7060,
    )
    assertEquals("Martin Freeman", mediaCrewResponseItem.name)
    assertEquals(7060, mediaCrewResponseItem.id)
    assertNull(mediaCrewResponseItem.originalName)
  }
}
