package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ContentRatingsItemResponse(

  @Json(name = "descriptors")
  val descriptors: List<Any?>? = null,

  @Json(name = "iso_3166_1")
  val iso31661: String? = null,

  @Json(name = "rating")
  val rating: String? = null
)
