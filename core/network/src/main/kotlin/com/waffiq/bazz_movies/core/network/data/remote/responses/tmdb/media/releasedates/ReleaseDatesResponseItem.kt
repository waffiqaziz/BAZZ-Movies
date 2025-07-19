package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ReleaseDatesResponseItem(
  @Json(name = "iso_3166_1")
  val iso31661: String? = null,

  @Json(name = "releasedates")
  val listReleaseDateResponseItemValue: List<ReleaseDatesResponseItemValue>? = null,
)
