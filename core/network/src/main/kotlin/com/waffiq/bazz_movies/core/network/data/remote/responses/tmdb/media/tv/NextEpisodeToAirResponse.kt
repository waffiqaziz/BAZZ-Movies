package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NextEpisodeToAirResponse(
  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "air_date")
  val airDate: String? = null,
)
