package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductionCountriesItemTest {

  @Test
  fun createProductionCountriesItem_withNoParameters_createsInstanceSuccessfully() {
    val productionCountry = ProductionCountriesItem()

    assertNull(productionCountry.iso31661)
    assertNull(productionCountry.name)
    assertNull(productionCountry.type)
    assertNull(productionCountry.iso6391)
    assertNull(productionCountry.certification)
  }

  @Test
  fun createProductionCountriesItem_withAllParameters_createsInstanceSuccessfully() {
    val productionCountry = ProductionCountriesItem(
      iso31661 = "US",
      name = "United States",
      type = 1,
      iso6391 = "en",
      certification = "PG-13"
    )

    assertEquals("US", productionCountry.iso31661)
    assertEquals("United States", productionCountry.name)
    assertEquals(1, productionCountry.type)
    assertEquals("en", productionCountry.iso6391)
    assertEquals("PG-13", productionCountry.certification)
  }

  @Test
  fun createProductionCountriesItem_withOnlyName_createsInstanceSuccessfully() {
    val productionCountry = ProductionCountriesItem(name = "Canada")

    assertNull(productionCountry.iso31661)
    assertEquals("Canada", productionCountry.name)
    assertNull(productionCountry.type)
    assertNull(productionCountry.iso6391)
    assertNull(productionCountry.certification)
  }

  @Test
  fun createProductionCountriesItem_withExplicitNulls_createsInstanceSuccessfully() {
    val productionCountry = ProductionCountriesItem(
      iso31661 = null,
      name = null,
      type = null,
      iso6391 = null,
      certification = null
    )

    assertNull(productionCountry.iso31661)
    assertNull(productionCountry.name)
    assertNull(productionCountry.type)
    assertNull(productionCountry.iso6391)
    assertNull(productionCountry.certification)
  }
}
