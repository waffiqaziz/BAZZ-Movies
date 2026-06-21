package com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchProvidersTest {

  @Test
  fun createWatchProviders_withValidValues_setsPropertiesCorrectly() {
    val results = mapOf("US" to WatchProvidersItem(null, null, null, null, null, null))
    val watchProviders = WatchProviders(results = results)

    assertEquals(results, watchProviders.results)
  }

  @Test
  fun createWatchProviders_withNullId_setsIdToNull() {
    val results = mapOf("US" to WatchProvidersItem(null, null, null, null, null, null))
    val watchProviders = WatchProviders(results = results)

    assertEquals(results, watchProviders.results)
  }

  @Test
  fun createWatchProviders_withNullResults_setsResultsToNull() {
    val watchProviders = WatchProviders(results = null)
    assertNull(watchProviders.results)
  }

  @Test
  fun createWatchProviders_withBothNull_setsAllPropertiesToNull() {
    val watchProviders = WatchProviders(results = null)
    assertNull(watchProviders.results)
  }

  @Test
  fun createWatchProviders_withEmptyResults_setsEmptyMap() {
    val watchProviders = WatchProviders(results = emptyMap())
    assertTrue(watchProviders.results?.isEmpty() == true)
  }
}
