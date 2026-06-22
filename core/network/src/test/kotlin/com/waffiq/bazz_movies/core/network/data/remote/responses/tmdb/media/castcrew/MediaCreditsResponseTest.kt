package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCastItemResponseDump
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.mediaCrewItemResponseDump
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MediaCreditsResponseTest {

  @Test
  fun mediaCreditsResponse_withValidValues_setsPropertiesCorrectly() {
    val mediaCreditsResponse = MediaCreditsResponse(
      crew = listOf(mediaCrewItemResponseDump),
      cast = listOf(mediaCastItemResponseDump),
    )
    assertEquals("Alexa Goodall", mediaCreditsResponse.cast[0].name)
    assertEquals("Frank Schlegel", mediaCreditsResponse.crew[0].name)
  }

  @Test
  fun mediaCreditsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val mediaCreditsResponse = MediaCreditsResponse(
      cast = emptyList(),
      crew = emptyList(),
    )
    assertEquals(emptyList<MediaCastResponseItem>(), mediaCreditsResponse.cast)
  }
}
