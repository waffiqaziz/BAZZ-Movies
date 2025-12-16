package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.ratedResponse
import org.junit.Assert.assertSame
import org.junit.Test
import kotlin.test.assertEquals

class RatedResponseTest {

  @Test
  fun mediaStateResponse_withValidValues_setsPropertiesCorrectly() {
    val data = ratedResponse
    assertEquals(data.value, 5.0)

    val data2 = RatedResponse.Unrated
    assertSame(RatedResponse.Unrated, data2)
  }
}
