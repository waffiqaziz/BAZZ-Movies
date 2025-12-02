package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VideoItemTest {

  @Test
  fun createVideoItem_withOnlyRequiredName_createsInstanceSuccessfully() {
    val videoItem = VideoItem(name = "Test Video")

    assertEquals("Test Video", videoItem.name)
    assertNull(videoItem.site)
    assertNull(videoItem.size)
    assertNull(videoItem.iso31661)
    assertNull(videoItem.official)
    assertNull(videoItem.id)
    assertNull(videoItem.publishedAt)
    assertNull(videoItem.type)
    assertNull(videoItem.iso6391)
    assertNull(videoItem.key)
  }

  @Test
  fun createVideoItem_withAllParameters_createsInstanceSuccessfully() {
    val videoItem = VideoItem(
      site = "YouTube",
      size = 1080,
      iso31661 = "US",
      name = "Test Video",
      official = true,
      id = "12345",
      publishedAt = "2024-01-01T00:00:00Z",
      type = "Trailer",
      iso6391 = "en",
      key = "dQw4w9WgXcQ"
    )

    assertEquals("YouTube", videoItem.site)
    assertEquals(1080, videoItem.size)
    assertEquals("US", videoItem.iso31661)
    assertEquals("Test Video", videoItem.name)
    assertEquals(true, videoItem.official)
    assertEquals("12345", videoItem.id)
    assertEquals("2024-01-01T00:00:00Z", videoItem.publishedAt)
    assertEquals("Trailer", videoItem.type)
    assertEquals("en", videoItem.iso6391)
    assertEquals("dQw4w9WgXcQ", videoItem.key)
  }

  @Test
  fun videoItemDestructuring_withAllComponents_extractsCorrectValues() {
    val videoItem = VideoItem(
      site = "YouTube",
      size = 1080,
      iso31661 = "US",
      name = "Test",
      official = true,
      id = "123",
      publishedAt = "2024-01-01",
      type = "Trailer",
      iso6391 = "en",
      key = "abc123"
    )

    assertEquals("YouTube", videoItem.site)
    assertEquals(1080, videoItem.size)
    assertEquals("US", videoItem.iso31661)
    assertEquals("Test", videoItem.name)
    assertEquals(true, videoItem.official)
    assertEquals("123", videoItem.id)
    assertEquals("2024-01-01", videoItem.publishedAt)
    assertEquals("Trailer", videoItem.type)
    assertEquals("en", videoItem.iso6391)
    assertEquals("abc123", videoItem.key)
  }

  @Test
  fun videoItemDestructuring_withNullValues_extractsNullValues() {
    val videoItem = VideoItem(name = "Test")

    assertNull(videoItem.site)
    assertNull(videoItem.size)
    assertNull(videoItem.iso31661)
    assertEquals("Test", videoItem.name)
    assertNull(videoItem.official)
    assertNull(videoItem.id)
    assertNull(videoItem.publishedAt)
    assertNull(videoItem.type)
    assertNull(videoItem.iso6391)
    assertNull(videoItem.key)
  }
}
