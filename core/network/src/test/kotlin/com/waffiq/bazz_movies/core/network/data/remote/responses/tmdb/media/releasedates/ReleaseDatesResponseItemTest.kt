package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReleaseDatesResponseItemTest {

  @Test
  fun releaseDatesResponseItem_withValidValues_setsPropertiesCorrectly() {
    val releaseDatesResponseItem = ReleaseDatesResponseItem(
      iso31661 = "ID",
      listReleaseDateResponseItemValue = listOf(
        ReleaseDatesResponseItemValue(note = "Disney+"),
      ),
    )
    assertEquals("ID", releaseDatesResponseItem.iso31661)
    assertEquals("Disney+", releaseDatesResponseItem.listReleaseDateResponseItemValue?.get(0)?.note)
  }

  @Test
  fun releaseDatesResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val releaseDatesResponseItem = ReleaseDatesResponseItem()
    assertNull(releaseDatesResponseItem.iso31661)
    assertNull(releaseDatesResponseItem.listReleaseDateResponseItemValue)
  }

  @Test
  fun releaseDatesResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val releaseDatesResponseItem = ReleaseDatesResponseItem("MY")
    assertEquals("MY", releaseDatesResponseItem.iso31661)
    assertNull(releaseDatesResponseItem.listReleaseDateResponseItemValue)
  }
}
