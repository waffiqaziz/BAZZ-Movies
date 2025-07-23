package com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CountryProviderDataTest {

  @Test
  fun createCountryProviderData_withAllNulls_createsInstanceSuccessfully() {
    val providerData = WatchProvidersItem(
      link = null,
      ads = null,
      buy = null,
      flatrate = null,
      free = null,
      rent = null
    )

    assertNull(providerData.link)
    assertNull(providerData.ads)
    assertNull(providerData.buy)
    assertNull(providerData.flatrate)
    assertNull(providerData.free)
    assertNull(providerData.rent)
  }

  @Test
  fun createCountryProviderData_withAllParameters_createsInstanceSuccessfully() {
    val provider = Provider(
      logoPath = "/logo.png",
      providerId = 1,
      providerName = "Netflix",
      displayPriority = 1
    )
    val providerData = WatchProvidersItem(
      link = "https://example.com",
      ads = listOf(provider),
      buy = listOf(provider),
      flatrate = listOf(provider),
      free = listOf(provider),
      rent = listOf(provider)
    )

    assertEquals("https://example.com", providerData.link)
    assertEquals(listOf(provider), providerData.ads)
    assertEquals(listOf(provider), providerData.buy)
    assertEquals(listOf(provider), providerData.flatrate)
    assertEquals(listOf(provider), providerData.free)
    assertEquals(listOf(provider), providerData.rent)
  }

  @Test
  fun createCountryProviderData_withEmptyLists_createsInstanceSuccessfully() {
    val providerData = WatchProvidersItem(
      link = "https://example.com",
      ads = emptyList(),
      buy = emptyList(),
      flatrate = emptyList(),
      free = emptyList(),
      rent = emptyList()
    )

    assertEquals("https://example.com", providerData.link)
    assertTrue(providerData.ads!!.isEmpty())
    assertTrue(providerData.buy!!.isEmpty())
    assertTrue(providerData.flatrate!!.isEmpty())
    assertTrue(providerData.free!!.isEmpty())
    assertTrue(providerData.rent!!.isEmpty())
  }

  @Test
  fun createCountryProviderData_withOnlyLink_createsInstanceSuccessfully() {
    val providerData = WatchProvidersItem(
      link = "https://tmdb.org",
      ads = null,
      buy = null,
      flatrate = null,
      free = null,
      rent = null
    )

    assertEquals("https://tmdb.org", providerData.link)
    assertNull(providerData.ads)
    assertNull(providerData.buy)
    assertNull(providerData.flatrate)
    assertNull(providerData.free)
    assertNull(providerData.rent)
  }
}
