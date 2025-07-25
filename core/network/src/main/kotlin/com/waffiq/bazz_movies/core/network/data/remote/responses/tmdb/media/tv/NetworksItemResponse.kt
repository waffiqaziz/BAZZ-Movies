package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class NetworksItemResponse(

  @Json(name = "logo_path")
  val logoPath: String? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "origin_country")
  val originCountry: String? = null
)
