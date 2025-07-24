package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VideoTest {

  @Test
  fun createVideo_withOnlyRequiredResults_createsInstanceSuccessfully() {
    val videoItems = listOf(VideoItem(name = "Test Video"))
    val video = Video(results = videoItems)

    assertNull(video.id)
    assertEquals(videoItems, video.results)
  }

  @Test
  fun createVideo_withAllParameters_createsInstanceSuccessfully() {
    val videoItems = listOf(VideoItem(name = "Test Video"))
    val video = Video(id = 123, results = videoItems)

    assertEquals(123, video.id)
    assertEquals(videoItems, video.results)
  }

  @Test
  fun createVideo_withNullId_createsInstanceSuccessfully() {
    val videoItems = listOf(VideoItem(name = "Test Video"))
    val video = Video(id = null, results = videoItems)

    assertNull(video.id)
    assertEquals(videoItems, video.results)
  }

  @Test
  fun createVideo_withEmptyResults_createsInstanceSuccessfully() {
    val video = Video(results = emptyList())

    assertNull(video.id)
    assertTrue(video.results.isEmpty())
  }
}
