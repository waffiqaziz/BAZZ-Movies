package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvCastResponseItem(

  @Json(name = "total_episode_count")
  val totalEpisodeCount: Int? = null,

  @Json(name = "gender")
  val gender: Int? = null,

  @Json(name = "known_for_department")
  val knownForDepartment: String? = null,

  @Json(name = "original_name")
  val originalName: String? = null,

  @Json(name = "popularity")
  val popularity: Double? = null,

  @Json(name = "roles")
  val roles: List<RolesResponseItem?>? = null,

  @Json(name = "name")
  val name: String? = null,

  @Json(name = "profile_path")
  val profilePath: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "adult")
  val adult: Boolean? = null,

  @Json(name = "order")
  val order: Int? = null,
)
