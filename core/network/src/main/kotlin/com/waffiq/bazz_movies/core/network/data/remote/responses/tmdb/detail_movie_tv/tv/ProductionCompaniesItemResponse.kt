package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ProductionCompaniesItemResponse(

  @Json(name = "logo_path")
  val logoPath: Any? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "origin_country")
  val originCountry: String? = null
)
