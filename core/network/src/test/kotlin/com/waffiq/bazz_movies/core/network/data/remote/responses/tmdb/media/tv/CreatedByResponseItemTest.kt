package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.createdByResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreatedByResponseItemTest {

  @Test
  fun createdByItemResponse_withValidValues_setsPropertiesCorrectly() {
    val createdByItemResponse = createdByResponseItem
    assertEquals(1, createdByItemResponse.gender)
    assertEquals("675abc88ccf4df966822ca59", createdByItemResponse.creditId)
    assertEquals("Kim Ji-woon", createdByItemResponse.name)
    assertEquals(null, createdByItemResponse.profilePath)
    assertEquals(2349392, createdByItemResponse.id)
  }

  @Test
  fun createdByItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val createdByItemResponse = CreatedByResponseItem()
    assertNull(createdByItemResponse.gender)
    assertNull(createdByItemResponse.creditId)
    assertNull(createdByItemResponse.name)
    assertNull(createdByItemResponse.profilePath)
    assertNull(createdByItemResponse.id)
  }

  @Test
  fun createdByItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val createdByItemResponse = CreatedByResponseItem(
      id = 435625,
    )
    assertEquals(435625, createdByItemResponse.id)
    assertNull(createdByItemResponse.name)
  }
}
