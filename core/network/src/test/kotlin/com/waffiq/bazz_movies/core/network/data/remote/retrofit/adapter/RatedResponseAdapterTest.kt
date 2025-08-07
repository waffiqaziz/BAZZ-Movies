package com.waffiq.bazz_movies.core.network.data.remote.retrofit.adapter

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class RatedResponseAdapterTest {

  private val adapter = RatedResponseAdapter()

  @Test
  fun fromJson_withNullInput_returnsUnrated() {
    val result = adapter.fromJson(null)
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_withEmptyMap_returnsUnrated() {
    assertUnrated(adapter.fromJson(emptyMap<String, Any>()))
  }

  @Test
  fun fromJson_withCorrectMap_returnsValueResponse() {
    val result = adapter.fromJson(mapOf("value" to 4.5))
    assertTrue(result is RatedResponse.Value)
    assertEquals(4.5, (result as RatedResponse.Value).value)
  }

  @Test
  fun fromJson_withInvalidValue_returnsUnrated() {
    assertUnrated(adapter.fromJson(mapOf("value" to "invalid")))
  }

  @Test
  fun fromJson_withBooleanFalse_returnsUnrated() {
    assertUnrated(adapter.fromJson(false))
  }

  @Test
  fun fromJson_withBooleanTrue_returnsUnrated() {
    assertUnrated(adapter.fromJson(true))
  }

  @Test
  fun fromJson_withUnknownType_returnsUnrated() {
    assertUnrated(adapter.fromJson("unexpected type"))
  }

  private fun assertUnrated(response: RatedResponse) {
    assertTrue(response is RatedResponse.Unrated)
  }
}
