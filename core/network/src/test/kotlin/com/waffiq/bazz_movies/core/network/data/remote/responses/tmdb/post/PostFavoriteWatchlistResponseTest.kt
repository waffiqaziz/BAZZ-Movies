package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class PostFavoriteWatchlistResponseTest {

  @Test
  fun postFavoriteWatchlistResponse_withValidValues_setsPropertiesCorrectly() {
    val postFavoriteWatchlistResponse = PostFavoriteWatchlistResponse(400, "Failed")
    assertEquals(400, postFavoriteWatchlistResponse.statusCode)
    assertEquals("Failed", postFavoriteWatchlistResponse.statusMessage)
  }

  @Test
  fun postFavoriteWatchlistResponse_withDefaultValues_setsPropertiesCorrectly() {
    val postFavoriteWatchlistResponse = PostFavoriteWatchlistResponse()
    assertNull(postFavoriteWatchlistResponse.statusCode)
    assertNull(postFavoriteWatchlistResponse.statusMessage)
  }

  @Test
  fun postFavoriteWatchlistResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val postFavoriteWatchlistResponse = PostFavoriteWatchlistResponse(
      statusCode = 200,
    )
    assertEquals(200, postFavoriteWatchlistResponse.statusCode)
    assertNull(postFavoriteWatchlistResponse.statusMessage)
  }
}
