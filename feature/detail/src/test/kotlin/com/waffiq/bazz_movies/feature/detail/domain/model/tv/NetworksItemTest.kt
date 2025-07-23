package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NetworksItemTest {

  @Test
  fun createNetworksItem_withValidValues_setsPropertiesCorrectly() {
    val networksItem = NetworksItem(
      logoPath = "/logo.jpg",
      name = "Netflix",
      id = 213,
      originCountry = "US"
    )

    assertEquals("/logo.jpg", networksItem.logoPath)
    assertEquals("Netflix", networksItem.name)
    assertEquals(213, networksItem.id)
    assertEquals("US", networksItem.originCountry)
  }

  @Test
  fun createNetworksItem_withDefaultValues_setsAllPropertiesToNull() {
    val networksItem = NetworksItem()

    assertNull(networksItem.logoPath)
    assertNull(networksItem.name)
    assertNull(networksItem.id)
    assertNull(networksItem.originCountry)
  }

  @Test
  fun createNetworksItem_withPartialValues_setsSpecifiedPropertiesOnly() {
    val networksItem = NetworksItem(
      name = "HBO",
      id = 49
    )

    assertNull(networksItem.logoPath)
    assertEquals("HBO", networksItem.name)
    assertEquals(49, networksItem.id)
    assertNull(networksItem.originCountry)
  }

  @Test
  fun createNetworksItem_withZeroId_setsZeroId() {
    val networksItem = NetworksItem(id = 0)

    assertEquals(0, networksItem.id)
  }

  @Test
  fun createNetworksItem_withEmptyStrings_setsEmptyStrings() {
    val networksItem = NetworksItem(
      logoPath = "",
      name = "",
      originCountry = ""
    )

    assertEquals("", networksItem.logoPath)
    assertEquals("", networksItem.name)
    assertEquals("", networksItem.originCountry)
  }

  @Test
  fun createNetworksItem_withNegativeId_setsNegativeId() {
    val networksItem = NetworksItem(id = -1)

    assertEquals(-1, networksItem.id)
  }
}
