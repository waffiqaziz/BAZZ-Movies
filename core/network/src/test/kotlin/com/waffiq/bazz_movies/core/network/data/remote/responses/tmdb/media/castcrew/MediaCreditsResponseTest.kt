package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCastItemResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCrewItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class MediaCreditsResponseTest {

  @Test
  fun mediaCreditsResponse_withValidValues_setsPropertiesCorrectly() {
    val mediaCreditsResponse = MediaCreditsResponse(
      crew = listOf(mediaCrewItemResponseDump),
      id = 543977290,
      cast = listOf(mediaCastItemResponseDump)
    )
    assertEquals("Alexa Goodall", mediaCreditsResponse.cast[0].name)
    assertEquals(543977290, mediaCreditsResponse.id)
    assertEquals("Frank Schlegel", mediaCreditsResponse.crew[0].name)
  }

  @Test
  fun mediaCreditsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val mediaCreditsResponse = MediaCreditsResponse(
      cast = emptyList(),
      crew = emptyList()
    )
    assertNull(mediaCreditsResponse.id)
  }

  @Test
  fun mediaCreditsResponse_withSomeEmptyValues_setsPropertiesCorrectly() {
    val mediaCreditsResponse = MediaCreditsResponse(
      id = 2,
      cast = emptyList(),
      crew = emptyList()
    )
    assertEquals(2, mediaCreditsResponse.id)
  }
}
