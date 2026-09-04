package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DummyData.imagePersonResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ImagePersonResponseTest {

  @Test
  fun imagePersonResponse_withValidValues_setsPropertiesCorrectly() {
    val imagePersonResponse = imagePersonResponse
    assertEquals(736, imagePersonResponse.profiles?.get(0)?.width)
  }

  @Test
  fun imagePersonResponse_withDefaultValues_setsPropertiesCorrectly() {
    val imagePersonResponse = ImagePersonResponse()
    assertNull(imagePersonResponse.profiles)
  }
}
