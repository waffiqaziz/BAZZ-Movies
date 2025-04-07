package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ProductionCountriesItemResponseTest {

  @Test
  fun productionCountriesItemResponse_withValidValues_setsPropertiesCorrectly() {
    val productionCountriesItemResponse = ProductionCountriesItemResponse(
      iso31661 = "US",
      name = "United States of America",
      type = 1,
      iso6391 = "",
      certification = "",
    )
    assertEquals("United States of America", productionCountriesItemResponse.name)
    assertEquals("US", productionCountriesItemResponse.iso31661)
    assertEquals(1, productionCountriesItemResponse.type)
    assertEquals("", productionCountriesItemResponse.iso6391)
    assertEquals("", productionCountriesItemResponse.certification)
  }

  @Test
  fun productionCountriesItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val productionCountriesItemResponse = ProductionCountriesItemResponse()
    assertNull(productionCountriesItemResponse.iso31661)
    assertNull(productionCountriesItemResponse.name)
    assertNull(productionCountriesItemResponse.type)
    assertNull(productionCountriesItemResponse.iso6391)
    assertNull(productionCountriesItemResponse.certification)
  }

  @Test
  fun productionCountriesItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val productionCountriesItemResponse = ProductionCountriesItemResponse(
      iso31661 = "GB",
      name = "United Kingdom",
      type = 2
    )
    assertEquals("United Kingdom", productionCountriesItemResponse.name)
    assertEquals("GB", productionCountriesItemResponse.iso31661)
    assertEquals(2, productionCountriesItemResponse.type)
    assertNull(productionCountriesItemResponse.iso6391)
    assertNull(productionCountriesItemResponse.certification)
  }
}
