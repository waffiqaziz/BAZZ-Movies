package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse

object PostMapper {
  fun PostResponse.toPost() = Post(
    success = success,
    statusCode = statusCode,
    statusMessage = statusMessage
  )
}
