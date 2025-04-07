package com.waffiq.bazz_movies.core.network.data.remote.retrofit.adapter

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class RatedResponseAdapterTest {

  private val adapter = RatedResponseAdapter()

  @Test
  fun fromJson_NullInput_ReturnsUnrated() {
    val result = adapter.fromJson(null)
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_EmptyMap_ReturnsUnrated() {
    val result = adapter.fromJson(emptyMap<String, Any>())
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_MapWithValue_ReturnsValueResponse() {
    val result = adapter.fromJson(mapOf("value" to 4.5))
    assertTrue(result is RatedResponse.Value)
    assertEquals(4.5, (result as RatedResponse.Value).value)
  }

  @Test
  fun fromJson_MapWithInvalidValue_ReturnsUnrated() {
    val result = adapter.fromJson(mapOf("value" to "invalid"))
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_BooleanFalse_ReturnsUnrated() {
    val result = adapter.fromJson(false)
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_BooleanTrue_ReturnsUnrated() {
    val result = adapter.fromJson(true)
    assertTrue(result is RatedResponse.Unrated)
  }

  @Test
  fun fromJson_UnknownType_ReturnsUnrated() {
    val result = adapter.fromJson("unexpected type")
    assertTrue(result is RatedResponse.Unrated)
  }
}
