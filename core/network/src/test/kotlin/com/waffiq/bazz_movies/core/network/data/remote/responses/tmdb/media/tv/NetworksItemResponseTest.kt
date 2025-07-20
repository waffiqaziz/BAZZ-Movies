package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.networksItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class NetworksItemResponseTest {

  @Test
  fun networksItemResponse_withValidValues_setsPropertiesCorrectly() {
    val networksItemResponse = networksItemResponseDump
    assertEquals("/pOSCKaZhndUFYtxHXjQOV6xJi1s.png", networksItemResponse.logoPath)
    assertEquals("MBC", networksItemResponse.name)
    assertEquals(97, networksItemResponse.id)
    assertEquals("KR", networksItemResponse.originCountry)
  }

  @Test
  fun networksItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val networksItemResponse = NetworksItemResponse()
    assertNull(networksItemResponse.logoPath)
    assertNull(networksItemResponse.name)
    assertNull(networksItemResponse.id)
    assertNull(networksItemResponse.originCountry)
  }

  @Test
  fun networksItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val networksItemResponse = NetworksItemResponse(
      id = 675674
    )
    assertEquals(675674, networksItemResponse.id)
    assertNull(networksItemResponse.name)
  }
}
