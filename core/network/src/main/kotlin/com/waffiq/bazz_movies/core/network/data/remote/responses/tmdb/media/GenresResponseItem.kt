package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenresResponseItem(

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,
)
