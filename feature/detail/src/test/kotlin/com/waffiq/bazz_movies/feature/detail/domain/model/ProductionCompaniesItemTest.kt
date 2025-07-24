package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductionCompaniesItemTest {

  @Test
  fun createProductionCompaniesItem_withNoParameters_createsInstanceSuccessfully() {
    val productionCompany = ProductionCompaniesItem()

    assertNull(productionCompany.logoPath)
    assertNull(productionCompany.name)
    assertNull(productionCompany.id)
    assertNull(productionCompany.originCountry)
  }

  @Test
  fun createProductionCompaniesItem_withAllParameters_createsInstanceSuccessfully() {
    val productionCompany = ProductionCompaniesItem(
      logoPath = "/logo.png",
      name = "Warner Bros",
      id = 123,
      originCountry = "US"
    )

    assertEquals("/logo.png", productionCompany.logoPath)
    assertEquals("Warner Bros", productionCompany.name)
    assertEquals(123, productionCompany.id)
    assertEquals("US", productionCompany.originCountry)
  }

  @Test
  fun createProductionCompaniesItem_withOnlyName_createsInstanceSuccessfully() {
    val productionCompany = ProductionCompaniesItem(name = "Disney")

    assertNull(productionCompany.logoPath)
    assertEquals("Disney", productionCompany.name)
    assertNull(productionCompany.id)
    assertNull(productionCompany.originCountry)
  }

  @Test
  fun createProductionCompaniesItem_withExplicitNulls_createsInstanceSuccessfully() {
    val productionCompany = ProductionCompaniesItem(
      logoPath = null,
      name = null,
      id = null,
      originCountry = null
    )

    assertNull(productionCompany.logoPath)
    assertNull(productionCompany.name)
    assertNull(productionCompany.id)
    assertNull(productionCompany.originCountry)
  }
}
