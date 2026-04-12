package com.waffiq.bazz_movies.navigation

enum class ListType {
  AIRING_THIS_WEEK,
  BY_GENRE,
  BY_KEYWORD,
  NOW_PLAYING,
  POPULAR,
  RECOMMENDATION,
  TOP_RATED,
  TRENDING,
  UPCOMING;

  fun shouldUpdateBackdrop() = when (this) {
    BY_GENRE -> false
    RECOMMENDATION -> false
    else -> true
  }
}
