package com.waffiq.bazz_movies.core.data.remote.responses.tmdb.detail_movie_tv.cast_crew

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MovieTvCrewItemResponse(

  @Json(name = "gender")
  val gender: Int? = null,

  @Json(name = "credit_id")
  val creditId: String? = null,

  @Json(name = "known_for_department")
  val knownForDepartment: String? = null,

  @Json(name = "original_name")
  val originalName: String? = null,

  @Json(name = "popularity")
  val popularity: Double? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "profile_path")
  val profilePath: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

  @Json(name = "department")
  val department: String? = null,

  @Json(name = "job")
  val job: String? = null
)
