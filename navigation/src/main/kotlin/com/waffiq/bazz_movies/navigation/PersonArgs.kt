package com.waffiq.bazz_movies.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonArgs(
  val id: Int,
  val name: String?,
  val originalName: String?,
  val profilePath: String?,
) : Parcelable
