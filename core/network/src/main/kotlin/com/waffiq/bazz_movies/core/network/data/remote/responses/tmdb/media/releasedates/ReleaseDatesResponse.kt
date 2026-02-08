package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.releasedates

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ReleaseDatesResponse(
  @Json(name = "results")
  val listReleaseDatesResponseItem: List<ReleaseDatesResponseItem?>? = null,
)
