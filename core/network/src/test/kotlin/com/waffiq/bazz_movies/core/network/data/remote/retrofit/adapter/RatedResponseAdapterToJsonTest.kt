package com.waffiq.bazz_movies.core.network.data.remote.retrofit.adapter

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RatedResponseAdapterToJsonTest {

  private val adapter = RatedResponseAdapter()

  @Test
  fun toJson_withValidResponse_returnsCorrectMap() {
    val response = RatedResponse.Value(4.5)
    val result = adapter.toJson(response)
    assertTrue(result is Map<*, *>)
    assertEquals(4.5, (result as Map<*, *>)["value"])
  }

  @Test
  fun toJson_withUnratedResponse_returnsFalse() {
    val response = RatedResponse.Unrated
    val result = adapter.toJson(response)
    assertEquals(false, result)
  }
}
