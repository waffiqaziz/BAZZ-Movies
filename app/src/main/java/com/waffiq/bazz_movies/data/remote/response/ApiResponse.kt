package com.waffiq.bazz_movies.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(

  @field:SerializedName("message")
  val message: Int,

  @field:SerializedName("status_message")
  val status_message: String,

  @field:SerializedName("success")
  val success: Boolean,
)

data class CreateTokenResponse(

  @field:SerializedName("success")
  val success: Boolean,

  @field:SerializedName("expire_at")
  val expire_at: String,

  @field:SerializedName("request_token")
  val request_token: String
)

data class GuestSessionResponse(

  @field:SerializedName("success")
  val success: Boolean,

  @field:SerializedName("guest_session_id")
  val guest_session_id: String,

  @field:SerializedName("expire_at")
  val expire_at: String,
)