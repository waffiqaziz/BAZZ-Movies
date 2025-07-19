package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class SeasonsItemResponse(

  @Json(name = "air_date")
  val airDate: String? = null,

  @Json(name = "overview")
  val overview: String? = null,

  @Json(name = "episode_count")
  val episodeCount: Int? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "season_number")
  val seasonNumber: Int? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null
)
