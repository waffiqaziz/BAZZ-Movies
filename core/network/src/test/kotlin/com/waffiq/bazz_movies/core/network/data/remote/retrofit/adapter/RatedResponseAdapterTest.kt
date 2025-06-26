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
    val result = adapter.fromJson(emptyMap<String, Any>())
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_withCorrectMap_returnsValueResponse() {
    val result = adapter.fromJson(mapOf("value" to 4.5))
    assertTrue(result is RatedResponse.Value)
    assertEquals(4.5, (result as RatedResponse.Value).value)
  }

  @Test
  fun fromJson_withInvalidValue_returnsUnrated() {
    val result = adapter.fromJson(mapOf("value" to "invalid"))
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_withBooleanFalse_returnsUnrated() {
    val result = adapter.fromJson(false)
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_withBooleanTrue_returnsUnrated() {
    val result = adapter.fromJson(true)
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_withUnknownType_returnsUnrated() {
    val result = adapter.fromJson("unexpected type")
    assertTrue(result is RatedResponse.Unrated)
  }
}
