package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.networkResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NetworksResponseItemTest {

  @Test
  fun networkResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals("/pOSCKaZhndUFYtxHXjQOV6xJi1s.png", networkResponseItem.logoPath)
    assertEquals("MBC", networkResponseItem.name)
    assertEquals(97, networkResponseItem.id)
    assertEquals("KR", networkResponseItem.originCountry)
  }

  @Test
  fun networkResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val networkResponseItem = NetworksResponseItem()
    assertNull(networkResponseItem.logoPath)
    assertNull(networkResponseItem.name)
    assertNull(networkResponseItem.id)
    assertNull(networkResponseItem.originCountry)
  }

  @Test
  fun networkResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val networkResponseItem = NetworksResponseItem(
      id = 675674,
    )
    assertEquals(675674, networkResponseItem.id)
    assertNull(networkResponseItem.name)
  }
}
