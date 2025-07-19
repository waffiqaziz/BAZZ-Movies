package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class VideoResponseTest {

  @Test
  fun videoResponse_withValidValues_setsPropertiesCorrectly() {
    val videoResponse = VideoResponse(
      id = 455765,
      results = listOf(VideoResponseItem(
        iso6391 = "en",
        name = "Trailer",
      ))
    )
    assertEquals(455765, videoResponse.id)
    assertEquals("en", videoResponse.results[0].iso6391)
    assertEquals("Trailer", videoResponse.results[0].name)
  }

  @Test
  fun videoResponse_withDefaultValues_setsPropertiesCorrectly() {
    val videoResponse = VideoResponse(      results = listOf(VideoResponseItem(
      iso6391 = "id",
      name = "Trailer Film",
    )))
    assertNull(videoResponse.id)
    assertNotNull(videoResponse.results)
  }
}
