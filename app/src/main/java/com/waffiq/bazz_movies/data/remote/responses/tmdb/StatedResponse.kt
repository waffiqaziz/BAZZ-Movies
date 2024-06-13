package com.waffiq.bazz_movies.data.remote.responses.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class StatedResponse(
  @Json(name="id")
  val id: Int,

  @Json(name="favorite")
  val favorite: Boolean,

  @Json(name="rated")
  val rated: Any,

  @Json(name="watchlist")
  val watchlist: Boolean,
)