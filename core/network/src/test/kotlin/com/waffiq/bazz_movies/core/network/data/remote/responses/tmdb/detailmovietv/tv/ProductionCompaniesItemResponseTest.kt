package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.productionCompaniesItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ProductionCompaniesItemResponseTest {

  @Test
  fun productionCompaniesItemResponse_withValidValues_setsPropertiesCorrectly() {
    val productionCompaniesItemResponse = productionCompaniesItemResponseDump
    assertEquals("/hUzeosd33nzE5MCNsZxCGEKTXaQ.png", productionCompaniesItemResponse.logoPath)
    assertEquals("Marvel Studios", productionCompaniesItemResponse.name)
    assertEquals(420, productionCompaniesItemResponse.id)
    assertEquals("US", productionCompaniesItemResponse.originCountry)
  }

  @Test
  fun productionCompaniesItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val productionCompaniesItemResponse = ProductionCompaniesItemResponse()
    assertNull(productionCompaniesItemResponse.logoPath)
    assertNull(productionCompaniesItemResponse.name)
    assertNull(productionCompaniesItemResponse.id)
    assertNull(productionCompaniesItemResponse.originCountry)
  }

  @Test
  fun productionCompaniesItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val productionCompaniesItemResponse = ProductionCompaniesItemResponse(
      id = 7658762
    )
    assertEquals(7658762, productionCompaniesItemResponse.id)
    assertNull(productionCompaniesItemResponse.name)
  }
}
