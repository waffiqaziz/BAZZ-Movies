package com.waffiq.bazz_movies.data.remote.post_body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class SessionIDPostModel(
  @Json(name = "session_id") val sessionID: String,
)