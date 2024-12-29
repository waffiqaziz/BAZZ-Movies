package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class GenresItemResponse(

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null
)
