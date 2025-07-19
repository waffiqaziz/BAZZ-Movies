package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class WatchProvidersResponse(

  @Json(name = "id")
  val id: Int?,

  @Json(name = "results")
  val results: Map<String, WatchProvidersResponseItem>?,
)
