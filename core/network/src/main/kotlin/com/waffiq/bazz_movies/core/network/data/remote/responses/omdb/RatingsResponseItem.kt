package com.waffiq.bazz_movies.core.network.data.remote.responses.omdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingsResponseItem(

  @Json(name = "Value")
  val value: String? = null,

  @Json(name = "Source")
  val source: String? = null,
)
