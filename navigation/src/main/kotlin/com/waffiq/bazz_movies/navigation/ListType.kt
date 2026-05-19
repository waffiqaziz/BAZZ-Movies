package com.waffiq.bazz_movies.navigation

enum class ListType {
  ANIME_THIS_SEASON,
  ANIME_ALL_TIME,
  AIRING_THIS_WEEK,
  BY_GENRE,
  BY_KEYWORD,
  COSTUME_DRAMA,
  DONGHUA,
  NOW_PLAYING,
  POPULAR,
  RECOMMENDATION,
  ROMANCE_DRAMA,
  REALITY_SHOW,
  TOP_RATED,
  TRENDING_WEEK,
  TRENDING_TODAY,
  UPCOMING,
  ;

    fun shouldUpdateBackdrop() =
    when (this) {
      BY_GENRE -> false
      RECOMMENDATION -> false
      else -> true
    }
}
