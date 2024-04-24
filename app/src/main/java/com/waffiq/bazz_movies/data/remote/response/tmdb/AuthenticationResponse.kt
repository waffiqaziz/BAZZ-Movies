package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class AuthenticationResponse(

  @Json(name="success")
  val success: Boolean,

  @Json(name="expire_at")
  val expireAt: String? = null,

  @Json(name="request_token")
  val requestToken: String? = null
)

@JsonClass(generateAdapter = false)
data class CreateSessionResponse(
  @Json(name="success")
  val success: Boolean,

  @Json(name="session_id")
  val sessionId: String,
)