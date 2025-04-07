package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.releasedates

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ReleaseDatesItemResponseTest {

  @Test
  fun releaseDatesItemResponse_withValidValues_setsPropertiesCorrectly() {
    val releaseDatesItemResponse = ReleaseDatesItemResponse(
      iso31661 = "ID",
      listReleaseDateItemValueResponse = listOf(
        ReleaseDatesItemValueResponse(note = "Disney+")
      )
    )
    assertEquals("ID", releaseDatesItemResponse.iso31661)
    assertEquals("Disney+", releaseDatesItemResponse.listReleaseDateItemValueResponse?.get(0)?.note)
  }

  @Test
  fun releaseDatesItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val releaseDatesItemResponse = ReleaseDatesItemResponse()
    assertNull(releaseDatesItemResponse.iso31661)
    assertNull(releaseDatesItemResponse.listReleaseDateItemValueResponse)
  }

  @Test
  fun releaseDatesItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val releaseDatesItemResponse = ReleaseDatesItemResponse("MY")
    assertEquals("MY", releaseDatesItemResponse.iso31661)
    assertNull(releaseDatesItemResponse.listReleaseDateItemValueResponse)
  }
}
