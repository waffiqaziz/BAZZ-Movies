package com.waffiq.bazz_movies.core.network.utils.mappers

import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoriteRequest
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toUpdateFavoriteParams
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toUpdateWatchlistParams
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistRequest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class NetworkMapperTest {

  @Test
  fun toFavoriteRequest_withValidValues_returnsFavoriteRequest() {
    val favoriteParams = UpdateFavoriteParams("movie", 12345, false)
    val favoriteRequest = favoriteParams.toFavoriteRequest()

    assertEquals(12345, favoriteRequest.mediaId)
    assertEquals("movie", favoriteRequest.mediaType)
    assertFalse(favoriteRequest.favorite)
  }

  @Test
  fun toWatchlistRequest_withValidValues_returnsWatchListRequest() {
    val watchlistParams = UpdateWatchlistParams("tv", 67890, true)
    val watchlistRequest = watchlistParams.toWatchlistRequest()

    assertEquals(67890, watchlistRequest.mediaId)
    assertEquals("tv", watchlistRequest.mediaType)
    assertTrue(watchlistRequest.watchlist)
  }

  @Test
  fun toFavoriteModel_withValidValues_returnsUpdateFavoriteParams() {
    val favoriteRequest = FavoriteRequest("movie", 1254543255, true)
    val favoriteParams = favoriteRequest.toUpdateFavoriteParams()

    assertEquals(1254543255, favoriteParams.mediaId)
    assertEquals("movie", favoriteParams.mediaType)
    assertTrue(favoriteParams.favorite)
  }

  @Test
  fun toWatchlistModel_withValidValues_returnsUpdateWatchlistParams() {
    val watchlistRequest = WatchlistRequest("tv", 566536534, true)
    val watchlistParams = watchlistRequest.toUpdateWatchlistParams()

    assertEquals(566536534, watchlistParams.mediaId)
    assertEquals("tv", watchlistParams.mediaType)
    assertTrue(watchlistParams.watchlist)
  }
}
