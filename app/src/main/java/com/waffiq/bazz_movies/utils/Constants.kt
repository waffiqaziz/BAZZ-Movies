package com.waffiq.bazz_movies.utils

@Suppress("SpellCheckingInspection")
object Constants{
  const val TABLE_NAME = "favorite"
  const val INITIAL_PAGE_INDEX = 1
  const val DELAY_TIME = 2000L
  const val TMDB_LINK_SIGNUP = "https://www.themoviedb.org/signup"
  const val TMDB_LINK_FORGET_PASSWORD = "https://www.themoviedb.org/reset-password"
  const val TMDB_IMG_LINK_BACKDROP_W300 = "http://image.tmdb.org/t/p/w300/"
  const val TMDB_IMG_LINK_BACKDROP_W780 = "http://image.tmdb.org/t/p/w780/"
  const val TMDB_IMG_LINK_POSTER_W185 = "http://image.tmdb.org/t/p/w185/"
  const val TMDB_IMG_LINK_POSTER_W500 = "http://image.tmdb.org/t/p/w500/"
  const val YOUTUBE_LINK_TRAILER = "https://www.youtube.com/watch?v="
  const val GRAVATAR_LINK = "https://secure.gravatar.com/avatar/"
  const val COUNTRY_API_LINK = " https://api.country.is"

  val tabHomeHeadingArray = arrayOf(
    "Featured",
    "Movies",
    "TV Series"
  )
  val tabMoviesTvHeadingArray = arrayOf(
    "Movies",
    "TV Series"
  )
}