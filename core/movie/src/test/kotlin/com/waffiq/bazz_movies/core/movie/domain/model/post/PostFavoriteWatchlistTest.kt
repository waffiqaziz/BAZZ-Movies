package com.waffiq.bazz_movies.core.movie.domain.model.post

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PostFavoriteWatchlistTest {

  @Test
  fun postFavoriteWatchlist_withAllPropertiesSet() {
    val postFavoriteWatchlist = PostFavoriteWatchlist(
      statusCode = 200,
      statusMessage = "Success"
    )
    assertEquals("Success", postFavoriteWatchlist.statusMessage)
    assertEquals(200, postFavoriteWatchlist.statusCode)
  }

  @Test
  fun postFavoriteWatchlist_withDefaultValue() {
    val postFavoriteWatchlist = PostFavoriteWatchlist()
    assertNull(postFavoriteWatchlist.statusMessage)
    assertNull(postFavoriteWatchlist.statusCode)
  }
}
