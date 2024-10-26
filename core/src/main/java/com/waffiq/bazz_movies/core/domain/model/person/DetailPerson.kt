package com.waffiq.bazz_movies.core.domain.model.person

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailPerson(
  val alsoKnownAs: List<String?>? = null,
  val birthday: String? = null,
  val gender: Int? = null,
  val imdbId: String? = null,
  val knownForDepartment: String? = null,
  val profilePath: String? = null,
  val biography: String? = null,
  val deathday: String? = null,
  val placeOfBirth: String? = null,
  val popularity: Float? = null,
  val name: String? = null,
  val id: Int? = null,
  val adult: Boolean? = null,
  val homepage: String? = null
) : Parcelable
