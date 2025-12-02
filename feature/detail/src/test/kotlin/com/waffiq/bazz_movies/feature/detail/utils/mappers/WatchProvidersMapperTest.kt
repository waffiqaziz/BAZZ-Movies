package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.ProviderResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WatchProvidersMapperTest {

  @Test
  fun toWatchProviders_withValidValues_returnsWatchProviders() {
    val providerResponse = ProviderResponse(
      logoPath = "/logo.png",
      providerId = 1,
      providerName = "Netflix",
      displayPriority = 10
    )

    val countryProviderDataResponse = WatchProvidersResponseItem(
      link = "https://example.com",
      ads = listOf(providerResponse),
      buy = listOf(providerResponse),
      flatrate = listOf(providerResponse),
      free = listOf(providerResponse),
      rent = listOf(providerResponse)
    )

    val response = WatchProvidersResponse(
      id = 100,
      results = mapOf("US" to countryProviderDataResponse)
    )

    val domain = with(WatchProvidersMapper) { response.toWatchProviders() }
    assertEquals(100, domain.id)
    assertEquals(1, domain.results?.size)
    val country = domain.results?.get("US")
    assertEquals("https://example.com", country?.link)
    assertEquals(1, country?.ads?.size)
    assertEquals(1, country?.buy?.size)
    assertEquals(1, country?.flatrate?.size)
    assertEquals(1, country?.free?.size)
    assertEquals(1, country?.rent?.size)

    val provider = country?.ads?.first()
    assertEquals("/logo.png", provider?.logoPath)
    assertEquals(1, provider?.providerId)
    assertEquals("Netflix", provider?.providerName)
    assertEquals(10, provider?.displayPriority)
  }

  @Test
  fun toWatchProviders_withNullValues_returnsDomainWithNullFields() {
    val countryProviderDataResponse = WatchProvidersResponseItem(
      link = null,
      ads = null,
      buy = null,
      flatrate = null,
      free = null,
      rent = null
    )

    val response = WatchProvidersResponse(
      id = 101,
      results = mapOf("ID" to countryProviderDataResponse)
    )

    val domain = with(WatchProvidersMapper) { response.toWatchProviders() }
    assertEquals(101, domain.id)
    assertEquals(1, domain.results?.size)
    val country = domain.results?.get("ID")
    assertNull(country?.link)
    assertNull(country?.ads)
    assertNull(country?.buy)
    assertNull(country?.flatrate)
    assertNull(country?.free)
    assertNull(country?.rent)
  }

  @Test
  fun toWatchProviders_withNullResults_returnsDomainWithNullResults() {
    val response = WatchProvidersResponse(
      id = 102,
      results = null
    )

    val domain = with(WatchProvidersMapper) { response.toWatchProviders() }
    assertEquals(102, domain.id)
    assertNull(domain.results)
  }
}
