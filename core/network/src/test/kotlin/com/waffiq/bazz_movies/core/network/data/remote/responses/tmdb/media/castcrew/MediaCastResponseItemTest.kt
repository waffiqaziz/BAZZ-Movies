package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DummyData.mediaCastResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaCastResponseItemTest {

  @Test
  fun mediaCastResponseItem_withValidValues_setsPropertiesCorrectly() {
    val mediaCastResponseItem = mediaCastResponseItem
    assertEquals(13, mediaCastResponseItem.castId)
    assertEquals("Momo", mediaCastResponseItem.character)
    assertEquals(1, mediaCastResponseItem.gender)
    assertEquals("6638f569ae38430122ca1143", mediaCastResponseItem.creditId)
    assertEquals("Acting", mediaCastResponseItem.knownForDepartment)
    assertEquals("Alexa Goodall", mediaCastResponseItem.originalName)
    assertEquals(3.822, mediaCastResponseItem.popularity)
    assertEquals("Alexa Goodall", mediaCastResponseItem.name)
    assertEquals("/39Pk0wdjD2TC4QgnrODxWD8bubH.jpg", mediaCastResponseItem.profilePath)
    assertEquals(3771374, mediaCastResponseItem.id)
    assertTrue(mediaCastResponseItem.adult == false)
    assertEquals(0, mediaCastResponseItem.order)
  }

  @Test
  fun mediaCastResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val mediaCastResponseItem = MediaCastResponseItem()
    assertNull(mediaCastResponseItem.castId)
    assertNull(mediaCastResponseItem.character)
    assertNull(mediaCastResponseItem.gender)
    assertNull(mediaCastResponseItem.creditId)
    assertNull(mediaCastResponseItem.knownForDepartment)
    assertNull(mediaCastResponseItem.originalName)
    assertNull(mediaCastResponseItem.popularity)
    assertNull(mediaCastResponseItem.name)
    assertNull(mediaCastResponseItem.profilePath)
    assertNull(mediaCastResponseItem.id)
    assertNull(mediaCastResponseItem.adult)
    assertNull(mediaCastResponseItem.order)
  }

  @Test
  fun mediaCastResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val mediaCastResponseItem = MediaCastResponseItem(
      name = "Martin Freeman",
    )
    assertEquals("Martin Freeman", mediaCastResponseItem.name)
    assertNull(mediaCastResponseItem.originalName)
  }
}
