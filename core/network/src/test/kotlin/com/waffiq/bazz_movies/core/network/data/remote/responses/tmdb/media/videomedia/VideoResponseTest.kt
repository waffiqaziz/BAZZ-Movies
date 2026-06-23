package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class VideoResponseTest {

  @Test
  fun videoResponse_withValidValues_setsPropertiesCorrectly() {
    val videoResponse = VideoResponse(
      results = listOf(
        VideoResponseItem(
          iso6391 = "en",
          name = "Trailer",
        ),
      ),
    )
    assertEquals("en", videoResponse.results[0].iso6391)
    assertEquals("Trailer", videoResponse.results[0].name)
  }

  @Test
  fun videoResponse_withDefaultValues_setsPropertiesCorrectly() {
    val videoResponse = VideoResponse(
      results = listOf(
        VideoResponseItem(
          iso6391 = "id",
          name = "Trailer Film",
        ),
      ),
    )
    assertNotNull(videoResponse.results)
  }
}
