package com.waffiq.bazz_movies.feature.detail.domain.model.tv

data class CrewItem(
  val totalEpisodeCount: Int? = null,
  val gender: Int? = null,
  val knownForDepartment: String? = null,
  val originalName: String? = null,
  val popularity: Any? = null,
  val jobs: List<JobsItem?>? = null,
  val name: String? = null,
  val profilePath: String? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val department: String? = null,
)
