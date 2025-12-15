package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchProvidersResponseTest {

  @Test
  fun providerResponse_whenCreated_shouldHaveCorrectProperties() {
    val provider = ProviderResponse(
      logoPath = "/logo.jpg",
      providerId = 123,
      providerName = "Netflix",
      displayPriority = 1
    )

    assertEquals("/logo.jpg", provider.logoPath)
    assertEquals(123, provider.providerId)
    assertEquals("Netflix", provider.providerName)
    assertEquals(1, provider.displayPriority)
  }

  @Test
  fun providerResponse_whenCreatedWithNulls_shouldAllowNullValues() {
    val provider = ProviderResponse(
      logoPath = null,
      providerId = null,
      providerName = null,
      displayPriority = null
    )

    assertNull(provider.logoPath)
    assertNull(provider.providerId)
    assertNull(provider.providerName)
    assertNull(provider.displayPriority)
  }

  @Test
  fun watchProvidersResponseItem_whenCreated_shouldHaveCorrectProperties() {
    val providers = listOf(
      ProviderResponse("/logo.jpg", 123, "Netflix", 1)
    )

    val item = WatchProvidersResponseItem(
      link = "https://example.com",
      ads = null,
      buy = providers,
      flatrate = providers,
      free = null,
      rent = null
    )

    assertEquals("https://example.com", item.link)
    assertNull(item.ads)
    assertEquals(1, item.buy?.size)
    assertEquals(1, item.flatrate?.size)
    assertNull(item.free)
    assertNull(item.rent)
  }

  @Test
  fun watchProvidersResponseItem_whenAllListsPopulated_shouldContainAllProviders() {
    val provider1 = ProviderResponse("/logo1.jpg", 1, "Netflix", 1)
    val provider2 = ProviderResponse("/logo2.jpg", 2, "Hulu", 2)

    val item = WatchProvidersResponseItem(
      link = "https://example.com",
      ads = listOf(provider1),
      buy = listOf(provider2),
      flatrate = listOf(provider1, provider2),
      free = listOf(provider1),
      rent = listOf(provider2)
    )

    assertEquals(1, item.ads?.size)
    assertEquals(1, item.buy?.size)
    assertEquals(2, item.flatrate?.size)
    assertEquals(1, item.free?.size)
    assertEquals(1, item.rent?.size)
  }

  @Test
  fun watchProvidersResponse_whenCreated_shouldHaveCorrectProperties() {
    val resultsMap = mapOf(
      "US" to WatchProvidersResponseItem(
        link = "https://example.com",
        ads = null,
        buy = null,
        flatrate = null,
        free = null,
        rent = null
      )
    )

    val response = WatchProvidersResponse(
      id = 456,
      results = resultsMap
    )

    assertEquals(456, response.id)
    assertEquals(1, response.results?.size)
    assertTrue(response.results?.containsKey("US") == true)
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

  @Test
  fun watchProvidersResponse_whenMultipleCountries_shouldContainAllCountries() {
    val usItem = WatchProvidersResponseItem(null, null, null, null, null, null)
    val ukItem = WatchProvidersResponseItem(null, null, null, null, null, null)

    val resultsMap = mapOf(
      "US" to usItem,
      "UK" to ukItem,
      "CA" to usItem
    )

    val response = WatchProvidersResponse(
      id = 100,
      results = resultsMap
    )

    assertEquals(3, response.results?.size)
    assertTrue(response.results?.containsKey("US") == true)
    assertTrue(response.results?.containsKey("UK") == true)
    assertTrue(response.results?.containsKey("CA") == true)
  }

  @Test
  fun providerResponse_whenCopied_shouldCreateNewInstance() {
    val original = ProviderResponse("/logo.jpg", 123, "Netflix", 1)
    val copied = original.copy(providerName = "Hulu")

    assertEquals("Netflix", original.providerName)
    assertEquals("Hulu", copied.providerName)
    assertEquals(original.providerId, copied.providerId)
  }
}