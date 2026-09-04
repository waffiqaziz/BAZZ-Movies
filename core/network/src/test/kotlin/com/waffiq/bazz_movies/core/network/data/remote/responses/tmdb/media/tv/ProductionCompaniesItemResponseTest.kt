package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.productionCompaniesResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductionCompaniesItemResponseTest {

  @Test
  fun productionCompaniesItemResponse_withValidValues_setsPropertiesCorrectly() {
    val productionCompaniesItemResponse = productionCompaniesResponseItem
    assertEquals("/hUzeosd33nzE5MCNsZxCGEKTXaQ.png", productionCompaniesItemResponse.logoPath)
    assertEquals("Marvel Studios", productionCompaniesItemResponse.name)
    assertEquals(420, productionCompaniesItemResponse.id)
    assertEquals("US", productionCompaniesItemResponse.originCountry)
  }

  @Test
  fun productionCompaniesItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val productionCompaniesItemResponse = ProductionCompaniesResponseItem()
    assertNull(productionCompaniesItemResponse.logoPath)
    assertNull(productionCompaniesItemResponse.name)
    assertNull(productionCompaniesItemResponse.id)
    assertNull(productionCompaniesItemResponse.originCountry)
  }

  @Test
  fun productionCompaniesItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val productionCompaniesItemResponse = ProductionCompaniesResponseItem(
      id = 7658762,
    )
    assertEquals(7658762, productionCompaniesItemResponse.id)
    assertNull(productionCompaniesItemResponse.name)
  }
}
