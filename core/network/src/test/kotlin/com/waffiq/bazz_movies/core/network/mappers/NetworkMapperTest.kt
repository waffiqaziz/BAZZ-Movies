package com.waffiq.bazz_movies.core.network.mappers

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoriteModel
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoritePostModel
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistModel
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistPostModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class NetworkMapperTest {

  @Test
  fun `map FavoriteModel to FavoritePostModel`() {
    val favoriteModel = FavoriteModel("movie", 12345, false)
    val favoritePostModel = favoriteModel.toFavoritePostModel()

    assertEquals(12345, favoritePostModel.mediaId)
    assertEquals("movie", favoritePostModel.mediaType)
    assertFalse(favoritePostModel.favorite)
  }

  @Test
  fun `map WatchlistModel to WatchlistPostModel`() {
    val watchlistModel = WatchlistModel("tv", 67890, true)
    val watchlistPostModel = watchlistModel.toWatchlistPostModel()

    assertEquals(67890, watchlistPostModel.mediaId)
    assertEquals("tv", watchlistPostModel.mediaType)
    assertTrue(watchlistPostModel.watchlist)
  }

  @Test
  fun `map FavoritePostModel to FavoriteModel`() {
    val favoritePostModel = FavoritePostModel("movie", 1254543255, true)
    val favoriteModel = favoritePostModel.toFavoriteModel()

    assertEquals(1254543255, favoriteModel.mediaId)
    assertEquals("movie", favoriteModel.mediaType)
    assertTrue(favoriteModel.favorite)
  }

  @Test
  fun `map WatchlistPostModel to WatchlistModel`() {
    val watchlistPostModel = WatchlistPostModel("tv", 566536534, true)
    val watchlistModel = watchlistPostModel.toWatchlistModel()

    assertEquals(566536534, watchlistModel.mediaId)
    assertEquals("tv", watchlistModel.mediaType)
    assertTrue(watchlistModel.watchlist)
  }
}
