package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.castItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.rolesItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CastItemTest {

  @Test
  fun castItem_withValidValues_getsPropertiesCorrectly() {
    assertEquals(334, castItem.id)
    assertEquals("Cast 1", castItem.name)
    assertEquals("Cast One", castItem.originalName)
    assertEquals(listOf(rolesItem), castItem.roles)
    assertEquals("acting", castItem.knownForDepartment)
    assertEquals(1, castItem.gender)
    assertEquals("path1.jpg", castItem.profilePath)
    assertEquals(false, castItem.adult)
    assertEquals(1, castItem.order)
  }

  @Test
  fun castItem_withDefaultValues_getsPropertiesCorrectly() {
    val castItem = CastItem()
    assertNull(castItem.totalEpisodeCount)
    assertNull(castItem.gender)
    assertNull(castItem.knownForDepartment)
    assertNull(castItem.originalName)
    assertNull(castItem.popularity)
    assertNull(castItem.roles)
    assertNull(castItem.name)
    assertNull(castItem.profilePath)
    assertNull(castItem.id)
    assertNull(castItem.adult)
    assertNull(castItem.order)
  }
}
