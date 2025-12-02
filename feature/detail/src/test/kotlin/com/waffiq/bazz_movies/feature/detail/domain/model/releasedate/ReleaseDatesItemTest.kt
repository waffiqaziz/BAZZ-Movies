package com.waffiq.bazz_movies.feature.detail.domain.model.releasedate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReleaseDatesItemTest {

  @Test
  fun createReleaseDatesItem_withDefaultValues_shouldCreateInstance() {
    val item = ReleaseDatesItem()

    assertNull(item.iso31661)
    assertNull(item.listReleaseDatesItemValue)
  }

  @Test
  fun createReleaseDatesItem_withAllValues_shouldSetPropertiesCorrectly() {
    val iso31661 = "US"
    val list = listOf(ReleaseDatesItemValue())

    val item = ReleaseDatesItem(
      iso31661 = iso31661,
      listReleaseDatesItemValue = list
    )

    assertEquals(iso31661, item.iso31661)
    assertEquals(list, item.listReleaseDatesItemValue)
  }
}
