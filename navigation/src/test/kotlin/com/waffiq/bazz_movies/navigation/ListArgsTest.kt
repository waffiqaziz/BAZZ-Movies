package com.waffiq.bazz_movies.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class ListArgsTest {

  @Test
  fun constructor_whenOptionalValuesNotProvided_expectedDefaultValuesUsed() {
    val args = ListArgs(
      listType = ListType.TRENDING_WEEK,
      mediaType = MediaSource.Trending,
      title = "Trending Movies",
    )

    assertEquals(ListType.TRENDING_WEEK, args.listType)
    assertEquals(MediaSource.Trending, args.mediaType)
    assertEquals("Trending Movies", args.title)
    assertEquals(-1, args.id)
    assertEquals("", args.backdrop)
  }

  @Test
  fun constructor_whenAllValuesProvided_expectedValuesStored() {
    val args = ListArgs(
      listType = ListType.TRENDING_WEEK,
      mediaType = MediaSource.Typed("movie"),
      title = "Action Movies",
      id = 28,
      backdrop = "/backdrop.jpg",
    )

    assertEquals(ListType.TRENDING_WEEK, args.listType)
    assertEquals(MediaSource.Typed("movie"), args.mediaType)
    assertEquals("Action Movies", args.title)
    assertEquals(28, args.id)
    assertEquals("/backdrop.jpg", args.backdrop)
  }
}
