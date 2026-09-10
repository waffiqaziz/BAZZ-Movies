package com.waffiq.bazz_movies.core.network.utils.mapper

import com.waffiq.bazz_movies.core.model.FavoriteParams
import com.waffiq.bazz_movies.core.model.WatchlistParams
import com.waffiq.bazz_movies.core.network.data.remote.model.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.model.WatchlistRequest

object NetworkMapper {

  fun FavoriteParams.toFavoriteRequest() =
    FavoriteRequest(
      mediaType = mediaType,
      mediaId = mediaId,
      favorite = favorite,
    )

  fun WatchlistParams.toWatchlistRequest() =
    WatchlistRequest(
      mediaType = mediaType,
      mediaId = mediaId,
      watchlist = watchlist,
    )

  fun FavoriteRequest.toUpdateFavoriteParams() =
    FavoriteParams(
      mediaType = mediaType,
      mediaId = mediaId,
      favorite = favorite,
    )

  fun WatchlistRequest.toUpdateWatchlistParams() =
    WatchlistParams(
      mediaType = mediaType,
      mediaId = mediaId,
      watchlist = watchlist,
    )
}
