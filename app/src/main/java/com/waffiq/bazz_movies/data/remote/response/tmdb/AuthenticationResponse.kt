package com.waffiq.bazz_movies.data.remote.response.tmdb

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(

  @field:SerializedName("success")
  val success: Boolean,

  @field:SerializedName("expire_at")
  val expireAt: String? = null,

  @field:SerializedName("request_token")
  val requestToken: String? = null
)

data class CreateSessionResponse(
  @field:SerializedName("success")
  val success: Boolean,

  @field:SerializedName("session_id")
  val sessionId: String,
)