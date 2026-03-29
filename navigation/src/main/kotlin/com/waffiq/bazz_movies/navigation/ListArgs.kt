package com.waffiq.bazz_movies.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListArgs(
  val listType: ListType,
  val mediaType: String,
  val title: String,
  val genreId: Int = -1,
  val keywordId: Int = -1,
) : Parcelable
