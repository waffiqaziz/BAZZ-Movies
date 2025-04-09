package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.videomedia

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.videoItemMovieResponseDump1
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class VideoItemResponseTest {

  @Test
  fun videoItemResponse_withValidValues_setsPropertiesCorrectly() {
    val videoItemResponse = videoItemMovieResponseDump1
    assertEquals(
      "'Oppenheimer' Wins Best Cinematography | 96th Oscars (2024)",
      videoItemResponse.name
    )
    assertEquals("YouTube", videoItemResponse.site)
    assertEquals(1080, videoItemResponse.size)
    assertEquals("US", videoItemResponse.iso31661)
    assertTrue(videoItemResponse.official == true)
    assertEquals("2024-04-11T19:00:07.000Z", videoItemResponse.publishedAt)
    assertEquals("Featurette", videoItemResponse.type)
    assertEquals("en", videoItemResponse.iso6391)
    assertEquals("O_hKC3gRvzw", videoItemResponse.key)
  }

  @Test
  fun videoItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val videoItemResponse = VideoItemResponse(
      name = "Trailer",
    )
    assertNotNull(videoItemResponse.name)
    assertNull(videoItemResponse.site)
    assertNull(videoItemResponse.size)
    assertNull(videoItemResponse.iso31661)
    assertNull(videoItemResponse.official)
    assertNull(videoItemResponse.id)
    assertNull(videoItemResponse.publishedAt)
    assertNull(videoItemResponse.type)
    assertNull(videoItemResponse.iso6391)
    assertNull(videoItemResponse.key)
  }
}
