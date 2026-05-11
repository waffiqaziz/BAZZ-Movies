package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.models.PostResult
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse

object PostMapper {
  fun PostResponse.toPostResult() =
    PostResult(
      success = success,
      statusCode = statusCode,
      statusMessage = statusMessage,
    )
}
