package com.waffiq.bazz_movies.core.network.utils.mapper

import com.waffiq.bazz_movies.core.model.media.MediaType
import com.waffiq.bazz_movies.core.model.user.FavoriteParams
import com.waffiq.bazz_movies.core.model.user.WatchlistParams
import com.waffiq.bazz_movies.core.network.utils.mapper.NetworkMapper.toFavoriteRequest
import com.waffiq.bazz_movies.core.network.utils.mapper.NetworkMapper.toMediaAccount
import com.waffiq.bazz_movies.core.network.utils.mapper.NetworkMapper.toWatchlistRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkMapperTest {

  @Test
  fun toFavoriteRequest_withValidValues_returnsFavoriteRequest() {
    val favoriteParams = FavoriteParams("movie", 12345, false)
    val favoriteRequest = favoriteParams.toFavoriteRequest()

    assertEquals(12345, favoriteRequest.mediaId)
    assertEquals("movie", favoriteRequest.mediaType)
    assertFalse(favoriteRequest.favorite)
  }

  @Test
  fun toWatchlistRequest_withValidValues_returnsWatchListRequest() {
    val watchlistParams = WatchlistParams("tv", 67890, true)
    val watchlistRequest = watchlistParams.toWatchlistRequest()

    assertEquals(67890, watchlistRequest.mediaId)
    assertEquals("tv", watchlistRequest.mediaType)
    assertTrue(watchlistRequest.watchlist)
  }

  @Test
  fun toMediaAccount_withValidMediaTypes_returnsCorrectParams() {
    assertEquals(MediaType.MOVIE.toMediaAccount, "movies")
    assertEquals(MediaType.TV.toMediaAccount, "tv")
  }
}
