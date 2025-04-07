package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ReleaseDatesResponseTest {

  @Test
  fun releaseDatesResponse_withValidValues_setsPropertiesCorrectly() {
    val releaseDatesResponse = ReleaseDatesResponse(
      listReleaseDatesItemResponse = listOf(
        ReleaseDatesItemResponse(
          iso31661 = "ID",
          listReleaseDateItemValueResponse = listOf(
            ReleaseDatesItemValueResponse(
              note = "Lembaga Sensor Film (LSF)"
            )
          )
        )
      )
    )
    assertEquals("ID", releaseDatesResponse.listReleaseDatesItemResponse?.get(0)?.iso31661)
    assertEquals(
      "Lembaga Sensor Film (LSF)",
      releaseDatesResponse
        .listReleaseDatesItemResponse?.get(0)?.listReleaseDateItemValueResponse?.get(0)?.note
    )
  }

  @Test
  fun releaseDatesResponse_withDefaultValues_setsPropertiesCorrectly() {
    val releaseDatesResponse = ReleaseDatesResponse()
    assertNull(releaseDatesResponse.listReleaseDatesItemResponse)
  }
}
