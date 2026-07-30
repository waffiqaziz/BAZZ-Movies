package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state

import com.waffiq.bazz_movies.core.network.testutils.DummyData.mediaStateResponse
import com.waffiq.bazz_movies.core.network.testutils.DummyData.ratedResponse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MediaStateResponseTest {

  @Test
  fun mediaStateResponse_withValidValues_setsPropertiesCorrectly() {
    val data = mediaStateResponse
    assertEquals(data.ratedResponse, ratedResponse)
    assertEquals(data.id, 453)
    assertTrue(data.favorite)
    assertTrue(data.watchlist)
  }
}
