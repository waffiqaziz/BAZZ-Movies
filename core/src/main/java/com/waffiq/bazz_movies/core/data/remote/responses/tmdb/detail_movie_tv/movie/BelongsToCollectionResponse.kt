package com.waffiq.bazz_movies.core.data.remote.responses.tmdb.detail_movie_tv.movie

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class BelongsToCollectionResponse(

  @Json(name = "backdrop_path")
  val backdropPath: String? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "poster_path")
  val posterPath: String? = null
)
