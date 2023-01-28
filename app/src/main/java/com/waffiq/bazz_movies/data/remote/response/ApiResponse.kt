package com.waffiq.bazz_movies.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(

  @field:SerializedName("success")
  val success: Boolean,

  @field:SerializedName("expire_at")
  val expire_at: String,

  @field:SerializedName("request_token")
  val request_token: String
)

data class CreateSessionResponse(
  @field:SerializedName("success")
  val success: Boolean,

  @field:SerializedName("session_id")
  val session_id: String,
)