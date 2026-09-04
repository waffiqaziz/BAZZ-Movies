package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.core.models.Nameable
import com.waffiq.bazz_movies.core.models.Profilable
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.RolesItem

data class MediaCastItem(
  override val name: String? = null,
  override val originalName: String? = null,
  override val profilePath: String? = null,
  val id: Int? = null,
  val castId: Int? = null,
  val character: String? = null,
  val roles: List<RolesItem?>? = null,
  val totalEpisodeCount: Int? = null,
  val gender: Int? = null,
  val creditId: String? = null,
  val knownForDepartment: String? = null,
  val popularity: Double? = null,
  val adult: Boolean? = null,
  val order: Int? = null,
) : Nameable,
  Profilable
