package com.waffiq.bazz_movies.core.data.remote.responses.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MovieTvResponse(

  @Json(name = "page")
  val page: Int,

  @Json(name = "total_pages")
  val totalPages: Int,

  @Json(name = "results")
  val results: List<ResultItemResponse>,

  @Json(name = "total_results")
  val totalResults: Int
)
