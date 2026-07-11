package com.waffiq.bazz_movies.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class MediaSourceTest {

  @Test
  fun typeName_whenTrending_expectedTrendingString() {
    val result = MediaSource.Trending.typeName

    assertEquals("trending", result)
  }

  @Test
  fun typeName_whenTyped_expectedMediaTypeString() {
    val mediaType = "movie"

    val result = MediaSource.Typed(mediaType).typeName

    assertEquals(mediaType, result)
  }
}
