package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailCollectionsResponse(

  @Json(name = "backdrop_path")
  val backdropPath: String? = null,

  @Json(name = "overview")
  val overview: String? = null,

  @Json(name = "original_language")
  val originalLanguage: String? = null,

  @Json(name = "original_name")
  val originalName: String? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "parts")
  val parts: List<PartsResponseItem?>? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null,
)
