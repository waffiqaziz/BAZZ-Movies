package com.waffiq.bazz_movies.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class MediaSource : Parcelable {

  @Parcelize
  data object Trending : MediaSource()

  @Parcelize
  data class Typed(val mediaType: String) : MediaSource()

  val typeName: String
    get() = when (this) {
      Trending -> "trending"
      is Typed -> mediaType
    }
}
