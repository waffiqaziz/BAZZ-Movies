package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.core.domain.Nameable
import com.waffiq.bazz_movies.core.domain.Profilable

data class MediaCrewItem(
  override val name: String? = null,
  override val originalName: String? = null,
  override val profilePath: String? = null,
  val id: Int? = null,
  val creditId: String? = null,
  val gender: Int? = null,
  val knownForDepartment: String? = null,
  val popularity: Double? = null,
  val adult: Boolean? = null,
  val department: String? = null,
  val job: String? = null,
) : Nameable,
  Profilable
