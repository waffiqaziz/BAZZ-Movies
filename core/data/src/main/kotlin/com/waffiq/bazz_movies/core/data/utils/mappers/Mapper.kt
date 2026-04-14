package com.waffiq.bazz_movies.core.data.utils.mappers

import com.waffiq.bazz_movies.core.data.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse

object Mapper {

  fun PostFavoriteWatchlistResponse.toPostFavoriteWatchlist() =
    PostFavoriteWatchlist(
      statusCode = statusCode,
      statusMessage = statusMessage,
    )
}
