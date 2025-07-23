package com.waffiq.bazz_movies.feature.detail.domain.model.releasedate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReleaseDatesTest {

  @Test
  fun createReleaseDates_withDefaultValues_shouldCreateInstance() {
    val item = ReleaseDates()
    assertNull(item.listReleaseDatesItem)
  }

  @Test
  fun createReleaseDates_withValidList_shouldSetPropertiesCorrectly() {
    val list = listOf(ReleaseDatesItem(), null)
    val item = ReleaseDates(listReleaseDatesItem = list)
    assertEquals(list, item.listReleaseDatesItem)
  }
}
