package com.waffiq.bazz_movies.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaCastItem(
  override val name: String? = null,
  override val originalName: String? = null,
  override val profilePath: String? = null,
  val id: Int? = null,
  val castId: Int? = null,
  val character: String? = null,
  val gender: Int? = null,
  val creditId: String? = null,
  val knownForDepartment: String? = null,
  val popularity: Double? = null,
  val adult: Boolean? = null,
  val order: Int? = null,
) : Nameable,
  Parcelable,
  Profilable
