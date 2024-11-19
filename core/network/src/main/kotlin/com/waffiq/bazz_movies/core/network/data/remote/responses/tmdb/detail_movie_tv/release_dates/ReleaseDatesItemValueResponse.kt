package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.release_dates

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ReleaseDatesItemValueResponse(
  @Json(name = "descriptors")
  val descriptors: List<Any?>? = null,

  @Json(name = "note")
  val note: String? = null,

  @Json(name = "type")
  val type: Int? = null,

  @Json(name = "iso_639_1")
  val iso6391: String? = null,

  @Json(name = "certification")
  val certification: String? = null,

  @Json(name = "release_date")
  val releaseDate: String? = null,
)
