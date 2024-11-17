package com.waffiq.bazz_movies.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieTvCastItem(
  val castId: Int? = null,
  val character: String? = null,
  val gender: Int? = null,
  val creditId: String? = null,
  val knownForDepartment: String? = null,
  val originalName: String? = null,
  val popularity: Double? = null,
  val name: String? = null,
  val profilePath: String? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val order: Int? = null
) : Parcelable
