package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreatedByItemTest {

  @Test
  fun createCreatedByItem_withValidValues_setsPropertiesCorrectly() {
    val createdByItem = CreatedByItem(
      gender = 1,
      creditId = "52fe4284c3a36847f8024f49",
      name = "Vince Gilligan",
      profilePath = "/profile.jpg",
      id = 66633
    )

    assertEquals(1, createdByItem.gender)
    assertEquals("52fe4284c3a36847f8024f49", createdByItem.creditId)
    assertEquals("Vince Gilligan", createdByItem.name)
    assertEquals("/profile.jpg", createdByItem.profilePath)
    assertEquals(66633, createdByItem.id)
  }

  @Test
  fun createCreatedByItem_withDefaultValues_setsAllPropertiesToNull() {
    val createdByItem = CreatedByItem()

    assertNull(createdByItem.gender)
    assertNull(createdByItem.creditId)
    assertNull(createdByItem.name)
    assertNull(createdByItem.profilePath)
    assertNull(createdByItem.id)
  }

  @Test
  fun createCreatedByItem_withPartialValues_setsSpecifiedPropertiesOnly() {
    val createdByItem = CreatedByItem(
      name = "David Chase",
      id = 1234
    )

    assertNull(createdByItem.gender)
    assertNull(createdByItem.creditId)
    assertEquals("David Chase", createdByItem.name)
    assertNull(createdByItem.profilePath)
    assertEquals(1234, createdByItem.id)
  }

  @Test
  fun createCreatedByItem_withZeroValues_setsZeroValues() {
    val createdByItem = CreatedByItem(
      gender = 0,
      id = 0
    )

    assertEquals(0, createdByItem.gender)
    assertEquals(0, createdByItem.id)
  }

  @Test
  fun createCreatedByItem_withEmptyStrings_setsEmptyStrings() {
    val createdByItem = CreatedByItem(
      creditId = "",
      name = "",
      profilePath = ""
    )

    assertEquals("", createdByItem.creditId)
    assertEquals("", createdByItem.name)
    assertEquals("", createdByItem.profilePath)
  }

  @Test
  fun createCreatedByItem_withNegativeValues_setsNegativeValues() {
    val createdByItem = CreatedByItem(
      gender = -1,
      id = -100
    )

    assertEquals(-1, createdByItem.gender)
    assertEquals(-100, createdByItem.id)
  }
}
