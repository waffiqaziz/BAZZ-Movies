package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MultiSearchResponse(

  @Json(name = "page")
  val page: Int? = null,

  @Json(name = "total_pages")
  val totalPages: Int? = null,

  @Json(name = "results")
  val results: List<ResultsItemSearchResponse>? = null,

  @Json(name = "total_results")
  val totalResults: Int? = null
)
