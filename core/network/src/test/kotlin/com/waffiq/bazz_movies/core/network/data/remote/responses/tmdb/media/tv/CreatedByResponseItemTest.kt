package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.createdByResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreatedByResponseItemTest {

  @Test
  fun createdByResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals(1, createdByResponseItem.gender)
    assertEquals("675abc88ccf4df966822ca59", createdByResponseItem.creditId)
    assertEquals("Kim Ji-woon", createdByResponseItem.name)
    assertEquals(null, createdByResponseItem.profilePath)
    assertEquals(2349392, createdByResponseItem.id)
  }

  @Test
  fun createdByResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val createdByResponseItem = CreatedByResponseItem()
    assertNull(createdByResponseItem.gender)
    assertNull(createdByResponseItem.creditId)
    assertNull(createdByResponseItem.name)
    assertNull(createdByResponseItem.profilePath)
    assertNull(createdByResponseItem.id)
  }

  @Test
  fun createdByResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val createdByResponseItem = CreatedByResponseItem(
      id = 435625,
    )
    assertEquals(435625, createdByResponseItem.id)
    assertNull(createdByResponseItem.name)
  }
}
