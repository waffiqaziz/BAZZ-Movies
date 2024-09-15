package com.waffiq.bazz_movies.data.remote.post_body

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class RatePostModel(
  @Json(name = "value") val value: Any,
)
