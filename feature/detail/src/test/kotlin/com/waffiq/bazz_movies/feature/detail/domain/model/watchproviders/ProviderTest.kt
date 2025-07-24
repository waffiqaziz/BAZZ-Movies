package com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProviderTest {

  @Test
  fun createProvider_withAllNulls_createsInstanceSuccessfully() {
    val provider = Provider(
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
  fun createProvider_withAllParameters_createsInstanceSuccessfully() {
    val provider = Provider(
      logoPath = "/logo.png",
      providerId = 123,
      providerName = "Netflix",
      displayPriority = 1
    )

    assertEquals("/logo.png", provider.logoPath)
    assertEquals(123, provider.providerId)
    assertEquals("Netflix", provider.providerName)
    assertEquals(1, provider.displayPriority)
  }

  @Test
  fun createProvider_withOnlyProviderName_createsInstanceSuccessfully() {
    val provider = Provider(
      logoPath = null,
      providerId = null,
      providerName = "Amazon Prime",
      displayPriority = null
    )

    assertNull(provider.logoPath)
    assertNull(provider.providerId)
    assertEquals("Amazon Prime", provider.providerName)
    assertNull(provider.displayPriority)
  }

  @Test
  fun createProvider_withMixedValues_createsInstanceSuccessfully() {
    val provider = Provider(
      logoPath = "/disney.png",
      providerId = null,
      providerName = "Disney+",
      displayPriority = 2
    )

    assertEquals("/disney.png", provider.logoPath)
    assertNull(provider.providerId)
    assertEquals("Disney+", provider.providerName)
    assertEquals(2, provider.displayPriority)
  }
}
