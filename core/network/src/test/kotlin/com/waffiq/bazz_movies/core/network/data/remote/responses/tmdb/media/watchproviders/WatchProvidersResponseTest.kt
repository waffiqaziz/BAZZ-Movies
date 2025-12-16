package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.providerResponse
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.providerResponseItem
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.watchProvidersResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchProvidersResponseTest {

  @Test
  fun providerResponse_whenCreated_shouldHaveCorrectProperties() {
    assertEquals("/logo.jpg", providerResponse.logoPath)
    assertEquals(123, providerResponse.providerId)
    assertEquals("Netflix", providerResponse.providerName)
    assertEquals(1, providerResponse.displayPriority)
  }

  @Test
  fun providerResponse_whenCreatedWithNulls_shouldAllowNullValues() {
    val providerNull = ProviderResponse(null, null, null, null)

    assertNull(providerNull.logoPath)
    assertNull(providerNull.providerId)
    assertNull(providerNull.providerName)
    assertNull(providerNull.displayPriority)
  }

  @Test
  fun watchProvidersResponseItem_whenCreated_shouldHaveCorrectProperties() {
    val providerItem = WatchProvidersResponseItem(null, null, null, null, null, null)

    assertNull(providerItem.link)
    assertNull(providerItem.ads)
    assertNull(providerItem.buy)
    assertNull(providerItem.flatrate)
    assertNull(providerItem.free)
    assertNull(providerItem.rent)
  }

  @Test
  fun watchProvidersResponseItem_whenAllListsPopulated_shouldContainAllProviders() {
    assertEquals(1, providerResponseItem.ads?.size)
    assertEquals(1, providerResponseItem.buy?.size)
    assertEquals(2, providerResponseItem.flatrate?.size)
    assertEquals(1, providerResponseItem.free?.size)
    assertEquals(1, providerResponseItem.rent?.size)
  }

  @Test
  fun watchProvidersResponse_whenCreated_shouldHaveCorrectProperties() {
    assertEquals(456, watchProvidersResponse.id)
    assertEquals(1, watchProvidersResponse.results?.size)
    assertTrue(watchProvidersResponse.results?.containsKey("US") == true)
  }

  @Test
  fun watchProvidersResponse_whenResultsEmpty_shouldHaveEmptyMap() {
    val response = WatchProvidersResponse(
      id = 789,
      results = emptyMap()
    )

    assertEquals(789, response.id)
    assertNotNull(response.results)
    assertTrue(response.results?.isEmpty() == true)
  }
}
