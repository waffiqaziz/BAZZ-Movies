package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MediaResponse(

  @Json(name = "page")
  val page: Int,

  @Json(name = "total_pages")
  val totalPages: Int,

  @Json(name = "results")
  val results: List<MediaResponseItem>,

  @Json(name = "total_results")
  val totalResults: Int,
)
