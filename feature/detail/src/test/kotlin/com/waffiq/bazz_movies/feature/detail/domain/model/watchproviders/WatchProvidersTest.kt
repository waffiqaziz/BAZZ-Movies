package com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchProvidersTest {

  @Test
  fun createWatchProviders_withValidValues_setsPropertiesCorrectly() {
    val id = 123
    val results = mapOf("US" to WatchProvidersItem(null, null, null, null, null, null))

    val watchProviders = WatchProviders(id = id, results = results)

    assertEquals(id, watchProviders.id)
    assertEquals(results, watchProviders.results)
  }

  @Test
  fun createWatchProviders_withNullId_setsIdToNull() {
    val results = mapOf("US" to WatchProvidersItem(null, null, null, null, null, null))

    val watchProviders = WatchProviders(id = null, results = results)

    assertNull(watchProviders.id)
    assertEquals(results, watchProviders.results)
  }

  @Test
  fun createWatchProviders_withNullResults_setsResultsToNull() {
    val watchProviders = WatchProviders(id = 123, results = null)

    assertEquals(123, watchProviders.id)
    assertNull(watchProviders.results)
  }

  @Test
  fun createWatchProviders_withBothNull_setsAllPropertiesToNull() {
    val watchProviders = WatchProviders(id = null, results = null)

    assertNull(watchProviders.id)
    assertNull(watchProviders.results)
  }

  @Test
  fun createWatchProviders_withEmptyResults_setsEmptyMap() {
    val watchProviders = WatchProviders(id = 123, results = emptyMap())

    assertEquals(123, watchProviders.id)
    assertTrue(watchProviders.results?.isEmpty() == true)
  }
}
