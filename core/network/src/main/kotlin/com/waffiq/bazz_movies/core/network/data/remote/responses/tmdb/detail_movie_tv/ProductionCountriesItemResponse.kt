package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ProductionCountriesItemResponse(

  @Json(name = "iso_3166_1")
  val iso31661: String? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "type")
  val type: Int? = null,

  @Json(name = "iso_639_1")
  val iso6391: String? = null,

  @Json(name = "certification")
  val certification: String? = null
)
