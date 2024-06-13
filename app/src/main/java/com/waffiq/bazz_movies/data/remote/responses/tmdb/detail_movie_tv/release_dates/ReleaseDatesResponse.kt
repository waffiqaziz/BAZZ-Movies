package com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.release_dates

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ReleaseDatesResponse(
  @Json(name="results")
  val listReleaseDatesItemResponse: List<ReleaseDatesItemResponse?>? = null
)