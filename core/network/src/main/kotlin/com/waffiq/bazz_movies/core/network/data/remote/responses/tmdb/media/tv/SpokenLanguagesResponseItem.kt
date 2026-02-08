package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class SpokenLanguagesResponseItem(

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "iso_639_1")
  val iso6391: String? = null,

  @Json(name = "english_name")
  val englishName: String? = null,
)
