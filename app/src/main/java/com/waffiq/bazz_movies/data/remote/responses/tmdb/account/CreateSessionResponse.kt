package com.waffiq.bazz_movies.data.remote.responses.tmdb.account

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class CreateSessionResponse(
  @Json(name="success")
  val success: Boolean,

  @Json(name="session_id")
  val sessionId: String
)