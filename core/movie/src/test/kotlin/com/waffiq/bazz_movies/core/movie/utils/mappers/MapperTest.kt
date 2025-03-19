package com.waffiq.bazz_movies.core.movie.utils.mappers

import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MapperTest {

  @Test
  fun toPostFavoriteWatchlist_returnCorrectly() {
    val postFavoriteWatchlistResponse = PostFavoriteWatchlistResponse(
      statusCode = 200,
      statusMessage = "success"
    )
    val postFavoriteWatchlist = postFavoriteWatchlistResponse.toPostFavoriteWatchlist()
    assertEquals(postFavoriteWatchlist.statusCode, 200)
    assertEquals(postFavoriteWatchlist.statusMessage, "success")
  }
}
