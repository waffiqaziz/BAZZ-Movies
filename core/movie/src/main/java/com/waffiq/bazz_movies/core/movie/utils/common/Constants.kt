package com.waffiq.bazz_movies.core.movie.utils.common

import com.waffiq.bazz_movies.core.ui.R.string.featured
import com.waffiq.bazz_movies.core.ui.R.string.movies
import com.waffiq.bazz_movies.core.ui.R.string.tv_series

object Constants {
  const val SWIPE_THRESHOLD = 0.3f
  const val FADE_ALPHA = 0.5f

  const val TMDB_IMG_LINK_BACKDROP_W300 = "https://image.tmdb.org/t/p/w300/"
  const val TMDB_IMG_LINK_BACKDROP_W780 = "https://image.tmdb.org/t/p/w780/"
  const val TMDB_IMG_LINK_POSTER_W185 = "https://image.tmdb.org/t/p/w185/"
  const val TMDB_IMG_LINK_POSTER_W500 = "https://image.tmdb.org/t/p/w500/"
  const val YOUTUBE_LINK_VIDEO = "https://www.youtube.com/watch?v="
  const val TMDB_IMG_LINK_AVATAR = "https://image.tmdb.org/t/p/w200/"
  const val GRAVATAR_LINK = "https://secure.gravatar.com/avatar/"



  const val DEBOUNCE_SHORT = 200L
  const val DEBOUNCE_LONG = 350L
  const val DEBOUNCE_VERY_LONG = 500L
  const val ANIM_DURATION = 200L

//  const val NOT_AVAILABLE = "N/A"

  const val NAN = "NaN"

  const val TERMS_CONDITIONS_LINK =
    "https://www.freeprivacypolicy.com/live/e3b6a0d4-ea46-4a95-914b-4120b0e83e9c"
  const val PRIVACY_POLICY_LINK =
    "https://www.freeprivacypolicy.com/live/c2970550-3a80-4c6e-99bb-51d81bfcd628"
  const val FORM_HELPER =
    "https://docs.google.com/forms/d/e/1FAIpQLScHL8ElRS4bqSkA__lGX97AnuJ0unA1DRAryIrgc72kuWOg_g/viewform?usp=sf_link"
  const val FAQ_LINK =
    "https://docs.google.com/document/d/1HNrj5i3Rnpr50Ldwgfz5ODpaJoWF17TXIop7xwtXkiU/edit?usp=sharing"
  const val TMDB_LINK_MAIN = "https://www.themoviedb.org/"
  const val BAZZ_MOVIES_LINK = "https://waffiqaziz.github.io/bazzmovies"

  val tabHomeHeadingArray = intArrayOf(featured, movies, tv_series)
  val tabMoviesTvHeadingArray = intArrayOf(movies, tv_series)
}
