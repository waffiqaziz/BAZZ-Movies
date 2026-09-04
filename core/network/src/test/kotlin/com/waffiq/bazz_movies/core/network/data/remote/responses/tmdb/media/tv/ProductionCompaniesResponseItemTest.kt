package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.productionCompaniesResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductionCompaniesResponseItemTest {

  @Test
  fun productionCompaniesResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals("/hUzeosd33nzE5MCNsZxCGEKTXaQ.png", productionCompaniesResponseItem.logoPath)
    assertEquals("Marvel Studios", productionCompaniesResponseItem.name)
    assertEquals(420, productionCompaniesResponseItem.id)
    assertEquals("US", productionCompaniesResponseItem.originCountry)
  }

  @Test
  fun productionCompaniesResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val productionCompaniesResponseItem = ProductionCompaniesResponseItem()
    assertNull(productionCompaniesResponseItem.logoPath)
    assertNull(productionCompaniesResponseItem.name)
    assertNull(productionCompaniesResponseItem.id)
    assertNull(productionCompaniesResponseItem.originCountry)
  }

  @Test
  fun productionCompaniesResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val productionCompaniesResponseItem = ProductionCompaniesResponseItem(
      id = 7658762,
    )
    assertEquals(7658762, productionCompaniesResponseItem.id)
    assertNull(productionCompaniesResponseItem.name)
  }
}
