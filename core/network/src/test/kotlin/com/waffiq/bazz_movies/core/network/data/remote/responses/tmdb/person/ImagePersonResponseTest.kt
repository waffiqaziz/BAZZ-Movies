package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.imagePersonResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class ImagePersonResponseTest {

  @Test
  fun imagePersonResponse_withValidValues_setsPropertiesCorrectly() {
    val imagePersonResponse = imagePersonResponseDump
    assertEquals(736, imagePersonResponse.profiles?.get(0)?.width)
    assertEquals(1878952, imagePersonResponse.id)
  }

  @Test
  fun imagePersonResponse_withDefaultValues_setsPropertiesCorrectly() {
    val imagePersonResponse = ImagePersonResponse()
    assertNull(imagePersonResponse.profiles)
    assertNull(imagePersonResponse.id)
  }

  @Test
  fun imagePersonResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val imagePersonResponse = ImagePersonResponse(
      profiles = null,
      id = 3267
    )
    assertNull(imagePersonResponse.profiles)
    assertEquals(3267, imagePersonResponse.id)
  }
}
