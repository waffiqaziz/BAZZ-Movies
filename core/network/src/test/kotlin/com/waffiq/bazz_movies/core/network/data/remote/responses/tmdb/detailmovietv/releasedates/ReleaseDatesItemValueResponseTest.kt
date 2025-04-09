package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ReleaseDatesItemValueResponseTest {

  @Test
  fun releaseDatesItemValueResponse_withValidValues_setsPropertiesCorrectly() {
    val releaseDatesItemValueResponse = ReleaseDatesItemValueResponse(
      descriptors = listOf("description1", "description2"),
      note = "(French speaking region)",
      type = 3,
      iso6391 = "FR",
      certification = "14",
      releaseDate = "2008-08-13T00:00:00.000Z",
    )
    assertEquals("description1", releaseDatesItemValueResponse.descriptors?.get(0))
    assertEquals("(French speaking region)", releaseDatesItemValueResponse.note)
    assertEquals(3, releaseDatesItemValueResponse.type)
    assertEquals("FR", releaseDatesItemValueResponse.iso6391)
    assertEquals("2008-08-13T00:00:00.000Z", releaseDatesItemValueResponse.releaseDate)
  }

  @Test
  fun releaseDatesItemValueResponse_withDefaultValues_setsPropertiesCorrectly() {
    val releaseDatesItemValueResponse = ReleaseDatesItemValueResponse()
    assertNull(releaseDatesItemValueResponse.descriptors)
    assertNull(releaseDatesItemValueResponse.note)
    assertNull(releaseDatesItemValueResponse.type)
    assertNull(releaseDatesItemValueResponse.iso6391)
    assertNull(releaseDatesItemValueResponse.certification)
    assertNull(releaseDatesItemValueResponse.releaseDate)
  }

  @Test
  fun releaseDatesItemValueResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val releaseDatesItemValueResponse = ReleaseDatesItemValueResponse(
      note = "(French speaking region)",
      iso6391 = "FR",
      certification = "14",
      releaseDate = "2008-08-13T00:00:00.000Z",
    )
    assertNull(releaseDatesItemValueResponse.descriptors)
    assertEquals("(French speaking region)", releaseDatesItemValueResponse.note)
    assertNull(releaseDatesItemValueResponse.type)
    assertEquals("FR", releaseDatesItemValueResponse.iso6391)
    assertEquals("2008-08-13T00:00:00.000Z", releaseDatesItemValueResponse.releaseDate)
  }
}
