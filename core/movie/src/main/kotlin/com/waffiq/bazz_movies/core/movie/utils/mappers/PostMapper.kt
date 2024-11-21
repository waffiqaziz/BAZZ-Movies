package com.waffiq.bazz_movies.core.movie.utils.mappers

import com.waffiq.bazz_movies.core.data.Post
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse

object PostMapper {

  fun PostFavoriteWatchlistResponse.toPostFavoriteWatchlist() = PostFavoriteWatchlist(
    statusCode = statusCode,
    statusMessage = statusMessage
  )

  fun PostResponse.toPost() = Post(
    success = success,
    statusCode = statusCode,
    statusMessage = statusMessage
  )
}
