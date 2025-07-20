package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCastItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MediaCastItemResponseTest {

  @Test
  fun mediaCastItemResponse_withValidValues_setsPropertiesCorrectly() {
    val mediaCastItemResponse = mediaCastItemResponseDump
    assertEquals(13, mediaCastItemResponse.castId)
    assertEquals("Momo", mediaCastItemResponse.character)
    assertEquals(1, mediaCastItemResponse.gender)
    assertEquals("6638f569ae38430122ca1143", mediaCastItemResponse.creditId)
    assertEquals("Acting", mediaCastItemResponse.knownForDepartment)
    assertEquals("Alexa Goodall", mediaCastItemResponse.originalName)
    assertEquals(3.822, mediaCastItemResponse.popularity)
    assertEquals("Alexa Goodall", mediaCastItemResponse.name)
    assertEquals("/39Pk0wdjD2TC4QgnrODxWD8bubH.jpg", mediaCastItemResponse.profilePath)
    assertEquals(3771374, mediaCastItemResponse.id)
    assertTrue(mediaCastItemResponse.adult == false)
    assertEquals(0, mediaCastItemResponse.order)
  }

  @Test
  fun mediaCastItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val mediaCastItemResponse = MediaCastResponseItem()
    assertNull(mediaCastItemResponse.castId)
    assertNull(mediaCastItemResponse.character)
    assertNull(mediaCastItemResponse.gender)
    assertNull(mediaCastItemResponse.creditId)
    assertNull(mediaCastItemResponse.knownForDepartment)
    assertNull(mediaCastItemResponse.originalName)
    assertNull(mediaCastItemResponse.popularity)
    assertNull(mediaCastItemResponse.name)
    assertNull(mediaCastItemResponse.profilePath)
    assertNull(mediaCastItemResponse.id)
    assertNull(mediaCastItemResponse.adult)
    assertNull(mediaCastItemResponse.order)
  }

  @Test
  fun mediaCastItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val mediaCastItemResponse = MediaCastResponseItem(
      name = "Martin Freeman"
    )
    assertEquals("Martin Freeman", mediaCastItemResponse.name)
    assertNull(mediaCastItemResponse.originalName)
  }
}
