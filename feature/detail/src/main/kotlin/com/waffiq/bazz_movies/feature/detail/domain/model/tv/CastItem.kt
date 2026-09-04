package com.waffiq.bazz_movies.feature.detail.domain.model.tv

data class CastItem(
  val totalEpisodeCount: Int? = null,
  val gender: Int? = null,
  val knownForDepartment: String? = null,
  val originalName: String? = null,
  val popularity: Any? = null,
  val roles: List<RolesItem?>? = null,
  val name: String? = null,
  val profilePath: String? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val order: Int? = null,
)
