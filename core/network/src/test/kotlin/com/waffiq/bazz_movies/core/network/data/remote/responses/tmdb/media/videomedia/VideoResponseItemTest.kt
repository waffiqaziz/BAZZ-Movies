package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia

import com.waffiq.bazz_movies.core.network.testutils.DummyData.videoItemMovieResponse1
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VideoResponseItemTest {

  @Test
  fun videoResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals(
      "'Oppenheimer' Wins Best Cinematography | 96th Oscars (2024)",
      videoItemMovieResponse1.name,
    )
    assertEquals("YouTube", videoItemMovieResponse1.site)
    assertEquals(1080, videoItemMovieResponse1.size)
    assertEquals("US", videoItemMovieResponse1.iso31661)
    assertTrue(videoItemMovieResponse1.official == true)
    assertEquals("2024-04-11T19:00:07.000Z", videoItemMovieResponse1.publishedAt)
    assertEquals("Featurette", videoItemMovieResponse1.type)
    assertEquals("en", videoItemMovieResponse1.iso6391)
    assertEquals("O_hKC3gRvzw", videoItemMovieResponse1.key)
  }

  @Test
  fun videoResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val videoResponseItem = VideoResponseItem(
      name = "Trailer",
    )
    assertNotNull(videoResponseItem.name)
    assertNull(videoResponseItem.site)
    assertNull(videoResponseItem.size)
    assertNull(videoResponseItem.iso31661)
    assertNull(videoResponseItem.official)
    assertNull(videoResponseItem.id)
    assertNull(videoResponseItem.publishedAt)
    assertNull(videoResponseItem.type)
    assertNull(videoResponseItem.iso6391)
    assertNull(videoResponseItem.key)
  }
}
