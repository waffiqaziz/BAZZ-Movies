package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatedResponse(
  @Json(name = "id")
  val id: Int,

  @Json(name = "favorite")
  val favorite: Boolean,

  @Json(name = "rated")
  val ratedResponse: RatedResponse,

  @Json(name = "watchlist")
  val watchlist: Boolean,
)
