package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.createdByItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CreatedByItemResponseTest {

  @Test
  fun createdByItemResponse_withValidValues_setsPropertiesCorrectly() {
    val createdByItemResponse = createdByItemResponseDump
    assertEquals(1, createdByItemResponse.gender)
    assertEquals("675abc88ccf4df966822ca59", createdByItemResponse.creditId)
    assertEquals("Kim Ji-woon", createdByItemResponse.name)
    assertEquals(null, createdByItemResponse.profilePath)
    assertEquals(2349392, createdByItemResponse.id)
  }

  @Test
  fun createdByItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val createdByItemResponse = CreatedByItemResponse()
    assertNull(createdByItemResponse.gender)
    assertNull(createdByItemResponse.creditId)
    assertNull(createdByItemResponse.name)
    assertNull(createdByItemResponse.profilePath)
    assertNull(createdByItemResponse.id)
  }

  @Test
  fun createdByItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val createdByItemResponse = CreatedByItemResponse(
      id = 435625
    )
    assertEquals(435625, createdByItemResponse.id)
    assertNull(createdByItemResponse.name)
  }
}
