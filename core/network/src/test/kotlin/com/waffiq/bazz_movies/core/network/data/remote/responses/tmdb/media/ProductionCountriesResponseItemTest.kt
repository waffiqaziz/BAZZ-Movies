package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductionCountriesResponseItemTest {

  @Test
  fun productionCountriesResponseItem_withValidValues_setsPropertiesCorrectly() {
    val productionCountriesResponseItem = ProductionCountriesResponseItem(
      iso31661 = "US",
      name = "United States of America",
      type = 1,
      iso6391 = "",
      certification = "",
    )
    assertEquals("United States of America", productionCountriesResponseItem.name)
    assertEquals("US", productionCountriesResponseItem.iso31661)
    assertEquals(1, productionCountriesResponseItem.type)
    assertEquals("", productionCountriesResponseItem.iso6391)
    assertEquals("", productionCountriesResponseItem.certification)
  }

  @Test
  fun productionCountriesResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val productionCountriesResponseItem = ProductionCountriesResponseItem()
    assertNull(productionCountriesResponseItem.iso31661)
    assertNull(productionCountriesResponseItem.name)
    assertNull(productionCountriesResponseItem.type)
    assertNull(productionCountriesResponseItem.iso6391)
    assertNull(productionCountriesResponseItem.certification)
  }

  @Test
  fun productionCountriesResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val productionCountriesResponseItem = ProductionCountriesResponseItem(
      iso31661 = "GB",
      name = "United Kingdom",
      type = 2,
    )
    assertEquals("United Kingdom", productionCountriesResponseItem.name)
    assertEquals("GB", productionCountriesResponseItem.iso31661)
    assertEquals(2, productionCountriesResponseItem.type)
    assertNull(productionCountriesResponseItem.iso6391)
    assertNull(productionCountriesResponseItem.certification)
  }
}
