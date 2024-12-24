package com.waffiq.bazz_movies.core.network.data.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class RatePostModel(
  @Json(name = "value") val value: Any,
)
