package com.waffiq.bazz_movies.feature.detail.domain.model.video

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VideosTest {

  @Test
  fun createVideo_withOnlyRequiredResults_createsInstanceSuccessfully() {
    val videoItems = listOf(VideoItem(name = "Test Video"))
    val video = Videos(results = videoItems)

    assertEquals(videoItems, video.results)
  }

  @Test
  fun createVideo_withAllParameters_createsInstanceSuccessfully() {
    val videoItems = listOf(VideoItem(name = "Test Video"))
    val video = Videos(results = videoItems)

    assertEquals(videoItems, video.results)
  }

  @Test
  fun createVideo_withNullId_createsInstanceSuccessfully() {
    val videoItems = listOf(VideoItem(name = "Test Video"))
    val video = Videos(results = videoItems)

    assertEquals(videoItems, video.results)
  }

  @Test
  fun createVideo_withEmptyResults_createsInstanceSuccessfully() {
    val video = Videos(results = emptyList())

    assertTrue(video.results.isEmpty())
  }
}
