package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaCastItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MediaCastItemTest {

  @Test
  fun mediaCastItem_withValidValue_returnsCorrectData() {
    assertEquals(123, mediaCastItem.castId)
    assertEquals("Joel Miller", mediaCastItem.character)
    assertEquals(2, mediaCastItem.gender)
    assertEquals("5e4b8a8f0c3a36847", mediaCastItem.creditId)
    assertEquals("Acting", mediaCastItem.knownForDepartment)
    assertEquals("José Pedro Balmaceda Pascal", mediaCastItem.originalName)
    assertEquals(87.42, mediaCastItem.popularity)
    assertEquals("Pedro Pascal", mediaCastItem.name)
    assertEquals("/profile.jpg", mediaCastItem.profilePath)
    assertEquals(125336, mediaCastItem.id)
    assertFalse(mediaCastItem.adult == true)
    assertEquals(0, mediaCastItem.order)
    assertEquals(9, mediaCastItem.totalEpisodeCount)

    assertNotNull(mediaCastItem.roles)
    assertEquals(1, mediaCastItem.roles?.size)

    val role = mediaCastItem.roles?.first()
    assertEquals("Joel Miller", role?.character)
    assertEquals(9, role?.episodeCount)
    assertEquals("5e4b8a8f0c3a36847", role?.creditId)
  }

  @Test
  fun mediaCastItem_withNullValue_returnsNull() {
    val mediaCastItemNull = MediaCastItem()
    assertNull(mediaCastItemNull.castId)
    assertNull(mediaCastItemNull.character)
    assertNull(mediaCastItemNull.gender)
    assertNull(mediaCastItemNull.creditId)
    assertNull(mediaCastItemNull.knownForDepartment)
    assertNull(mediaCastItemNull.originalName)
    assertNull(mediaCastItemNull.popularity)
    assertNull(mediaCastItemNull.name)
    assertNull(mediaCastItemNull.profilePath)
    assertNull(mediaCastItemNull.id)
    assertNull(mediaCastItemNull.adult)
    assertNull(mediaCastItemNull.order)
    assertNull(mediaCastItemNull.roles)
  }
}
