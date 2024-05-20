package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class PostResponse(
  @Json(name="status_code")
  val statusCode: Int? = null,

  @Json(name="status_message")
  val statusMessage: String? = null,
)

@JsonClass(generateAdapter = false)
data class PostRateResponse(
  @Json(name="success")
  val success: Boolean? = null,

  @Json(name="status_code")
  val statusCode: Int? = null,

  @Json(name="status_message")
  val statusMessage: String? = null,
)

