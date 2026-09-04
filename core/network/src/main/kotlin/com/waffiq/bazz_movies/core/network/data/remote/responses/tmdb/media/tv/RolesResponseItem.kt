package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RolesResponseItem(

  @Json(name = "character")
  val character: String? = null,

  @Json(name = "episode_count")
  val episodeCount: Int? = null,

  @Json(name = "credit_id")
  val creditId: String? = null,
)
