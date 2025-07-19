package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCrewItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class MediaCrewItemResponseTest {

  @Test
  fun mediaCrewItemResponse_withValidValues_setsPropertiesCorrectly() {
    val mediaCrewItemResponse = mediaCrewItemResponseDump
    assertEquals(0, mediaCrewItemResponse.gender)
    assertEquals("64fc09ebf85958011ca070b4", mediaCrewItemResponse.creditId)
    assertEquals("Visual Effects", mediaCrewItemResponse.knownForDepartment)
    assertEquals("Frank Schlegel", mediaCrewItemResponse.originalName)
    assertEquals(0.001, mediaCrewItemResponse.popularity)
    assertEquals("Frank Schlegel", mediaCrewItemResponse.name)
    assertEquals(null, mediaCrewItemResponse.profilePath)
    assertEquals(3014542, mediaCrewItemResponse.id)
    assertEquals(false, mediaCrewItemResponse.adult)
    assertEquals("Visual Effects", mediaCrewItemResponse.department)
    assertEquals("VFX Supervisor", mediaCrewItemResponse.job)
  }

  @Test
  fun mediaCrewItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val mediaCrewItemResponseNull = MediaCrewResponseItem()
    assertNull(mediaCrewItemResponseNull.gender)
    assertNull(mediaCrewItemResponseNull.creditId)
    assertNull(mediaCrewItemResponseNull.knownForDepartment)
    assertNull(mediaCrewItemResponseNull.originalName)
    assertNull(mediaCrewItemResponseNull.popularity)
    assertNull(mediaCrewItemResponseNull.name)
    assertNull(mediaCrewItemResponseNull.profilePath)
    assertNull(mediaCrewItemResponseNull.id)
    assertNull(mediaCrewItemResponseNull.adult)
    assertNull(mediaCrewItemResponseNull.department)
    assertNull(mediaCrewItemResponseNull.job)
  }

  @Test
  fun mediaCrewItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val mediaCrewItemResponse = MediaCrewResponseItem(
      name = "Martin Freeman",
      id = 7060

    )
    assertEquals("Martin Freeman", mediaCrewItemResponse.name)
    assertEquals(7060, mediaCrewItemResponse.id)
    assertNull(mediaCrewItemResponse.originalName)
  }
}
