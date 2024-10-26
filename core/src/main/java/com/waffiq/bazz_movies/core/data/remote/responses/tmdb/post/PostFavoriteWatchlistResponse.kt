package com.waffiq.bazz_movies.core.data.remote.responses.tmdb.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class PostFavoriteWatchlistResponse(
  @Json(name = "status_code")
  val statusCode: Int? = null,

  @Json(name = "status_message")
  val statusMessage: String? = null,
)
