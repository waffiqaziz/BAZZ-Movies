package com.waffiq.bazz_movies.core.utils.mappers

import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.domain.model.post.Post
import com.waffiq.bazz_movies.core.domain.model.post.PostFavoriteWatchlist

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
