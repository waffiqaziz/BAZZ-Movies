package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ReleaseDatesItemResponseTest {

  @Test
  fun releaseDatesItemResponse_withValidValues_setsPropertiesCorrectly() {
    val releaseDatesItemResponse = ReleaseDatesResponseItem(
      iso31661 = "ID",
      listReleaseDateResponseItemValue = listOf(
        ReleaseDatesResponseItemValue(note = "Disney+")
      )
    )
    assertEquals("ID", releaseDatesItemResponse.iso31661)
    assertEquals("Disney+", releaseDatesItemResponse.listReleaseDateResponseItemValue?.get(0)?.note)
  }

  @Test
  fun releaseDatesItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val releaseDatesItemResponse = ReleaseDatesResponseItem()
    assertNull(releaseDatesItemResponse.iso31661)
    assertNull(releaseDatesItemResponse.listReleaseDateResponseItemValue)
  }

  @Test
  fun releaseDatesItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val releaseDatesItemResponse = ReleaseDatesResponseItem("MY")
    assertEquals("MY", releaseDatesItemResponse.iso31661)
    assertNull(releaseDatesItemResponse.listReleaseDateResponseItemValue)
  }
}
