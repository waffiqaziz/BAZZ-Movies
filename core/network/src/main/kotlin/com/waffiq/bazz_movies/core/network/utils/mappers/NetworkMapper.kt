package com.waffiq.bazz_movies.core.network.utils.mappers

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistPostModel

object NetworkMapper {

  fun FavoriteModel.toFavoritePostModel() = FavoritePostModel(
    mediaType = mediaType,
    mediaId = mediaId,
    favorite = favorite,
  )

  fun WatchlistModel.toWatchlistPostModel() = WatchlistPostModel(
    mediaType = mediaType,
    mediaId = mediaId,
    watchlist = watchlist,
  )

  fun FavoritePostModel.toFavoriteModel() = FavoriteModel(
    mediaType = mediaType,
    mediaId = mediaId,
    favorite = favorite,
  )

  fun WatchlistPostModel.toWatchlistModel() = WatchlistModel(
    mediaType = mediaType,
    mediaId = mediaId,
    watchlist = watchlist,
  )
}
