package com.waffiq.bazz_movies.core.network.utils.mapper

import com.waffiq.bazz_movies.core.model.media.MediaType
import com.waffiq.bazz_movies.core.model.user.FavoriteParams
import com.waffiq.bazz_movies.core.model.user.WatchlistParams
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

  val MediaType.toMediaAccount: String
    get() = if (this == MediaType.MOVIE) "movies" else "tv"
}
