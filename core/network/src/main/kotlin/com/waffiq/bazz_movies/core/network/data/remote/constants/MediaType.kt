package com.waffiq.bazz_movies.core.network.data.remote.constants

enum class MediaType {
  MOVIES,
  TV,
  ;

    fun asApiValue(): String =
    when (this) {
      MOVIES -> "movies"
      TV -> "tv"
    }
}
