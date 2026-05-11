package com.waffiq.bazz_movies.core.network.utils.mappers

import com.waffiq.bazz_movies.core.models.FavoriteParams
import com.waffiq.bazz_movies.core.models.WatchlistParams
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest

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
