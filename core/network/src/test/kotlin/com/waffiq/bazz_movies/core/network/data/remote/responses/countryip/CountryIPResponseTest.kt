package com.waffiq.bazz_movies.core.network.data.remote.responses.countryip

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.countryIPResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CountryIPResponseTest {

  @Test
  fun countryIPResponse_withValidValues_setsPropertiesCorrectly() {
    val countryIPResponse = countryIPResponseDump
    assertEquals("ID", countryIPResponse.country)
    assertEquals("103.187.242.255", countryIPResponse.ip)
  }

  @Test
  fun countryIPResponse_withDefaultValues_setsPropertiesCorrectly() {
    val countryIPResponse = CountryIPResponse()
    assertNull(countryIPResponse.country)
    assertNull(countryIPResponse.ip)
  }

  @Test
  fun countryIPResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val countryIPResponse = CountryIPResponse(
      country = null,
      ip = "103.187.242.255"
    )
    assertNull(countryIPResponse.country)
    assertEquals("103.187.242.255", countryIPResponse.ip)
  }
}
