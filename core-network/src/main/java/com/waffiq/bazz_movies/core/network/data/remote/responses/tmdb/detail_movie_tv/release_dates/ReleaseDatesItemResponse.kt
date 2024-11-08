package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.release_dates

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ReleaseDatesItemResponse(
  @Json(name = "iso_3166_1")
  val iso31661: String? = null,

  @Json(name = "release_dates")
  val listReleaseDateItemValueResponse: List<ReleaseDatesItemValueResponse>? = null,
)
