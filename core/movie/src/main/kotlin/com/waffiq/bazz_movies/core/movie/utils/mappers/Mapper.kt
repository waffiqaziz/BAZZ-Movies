package com.waffiq.bazz_movies.core.movie.utils.mappers

import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse

object Mapper {

  fun PostFavoriteWatchlistResponse.toPostFavoriteWatchlist() =
    PostFavoriteWatchlist(
      statusCode = statusCode,
      statusMessage = statusMessage,
    )
}
