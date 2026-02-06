package com.waffiq.bazz_movies.core.network.utils.mappers

import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoriteRequest
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistRequest

object NetworkMapper {

  fun UpdateFavoriteParams.toFavoriteRequest() = FavoriteRequest(
    mediaType = mediaType,
    mediaId = mediaId,
    favorite = favorite,
  )

  fun UpdateWatchlistParams.toWatchlistRequest() = WatchlistRequest(
    mediaType = mediaType,
    mediaId = mediaId,
    watchlist = watchlist,
  )

  fun FavoriteRequest.toUpdateFavoriteParams() = UpdateFavoriteParams(
    mediaType = mediaType,
    mediaId = mediaId,
    favorite = favorite,
  )

  fun WatchlistRequest.toUpdateWatchlistParams() = UpdateWatchlistParams(
    mediaType = mediaType,
    mediaId = mediaId,
    watchlist = watchlist,
  )
}
