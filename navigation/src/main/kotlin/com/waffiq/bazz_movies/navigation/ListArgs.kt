package com.waffiq.bazz_movies.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListArgs(
  val listType: ListType,
  val mediaType: String,
  val title: String,
  val id: Int = -1, // used for genre, keywords, and recommendation id
  val backdrop: String = "",
) : Parcelable
