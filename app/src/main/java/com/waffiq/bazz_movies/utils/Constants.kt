package com.waffiq.bazz_movies.utils

import com.waffiq.bazz_movies.R.string.featured
import com.waffiq.bazz_movies.R.string.movies
import com.waffiq.bazz_movies.R.string.tv_series

object Constants {
  const val TABLE_NAME = "favorite"
  const val INITIAL_PAGE_INDEX = 1
  const val DELAY_TIME = 2000L
  const val NUM_TABS = 2
  const val TMDB_LINK_SIGNUP = "https://www.themoviedb.org/signup"
  const val TMDB_LINK_FORGET_PASSWORD = "https://www.themoviedb.org/reset-password"
  const val TMDB_IMG_LINK_BACKDROP_W300 = "http://image.tmdb.org/t/p/w300/"
  const val TMDB_IMG_LINK_BACKDROP_W780 = "http://image.tmdb.org/t/p/w780/"
  const val TMDB_IMG_LINK_POSTER_W185 = "http://image.tmdb.org/t/p/w185/"
  const val TMDB_IMG_LINK_POSTER_W500 = "http://image.tmdb.org/t/p/w500/"
  const val YOUTUBE_LINK_TRAILER = "https://www.youtube.com/watch?v="
  const val GRAVATAR_LINK = "https://secure.gravatar.com/avatar/"
  const val COUNTRY_API_LINK = "https://api.country.is"

  const val INSTAGRAM_LINK = "https://www.instagram.com/"
  const val X_LINK = "https://twitter.com/"
  const val FACEBOOK_LINK = "https://www.facebook.com/"
  const val TIKTOK_PERSON_LINK = "https://www.tiktok.com/@"
  const val YOUTUBE_CHANNEL_LINK = "https://www.youtube.com/"
  const val WIKIDATA_PERSON_LINK = "https://www.wikidata.org/wiki/"
  const val IMDB_PERSON_LINK = "https://www.imdb.com/name/"

  const val TERMS_CONDITIONS_LINK =
    "https://www.freeprivacypolicy.com/live/e3b6a0d4-ea46-4a95-914b-4120b0e83e9c"
  const val PRIVACY_POLICY_LINK =
    "https://www.freeprivacypolicy.com/live/c2970550-3a80-4c6e-99bb-51d81bfcd628"
  const val GMAIL_BAZZ_HELPER = "bazzmovies.help@gmail.com"
  const val FORM_HELPER =
    "https://docs.google.com/forms/d/e/1FAIpQLScHL8ElRS4bqSkA__lGX97AnuJ0unA1DRAryIrgc72kuWOg_g/viewform?usp=sf_link"
  const val FAQ_LINK =
    "https://docs.google.com/document/d/1HNrj5i3Rnpr50Ldwgfz5ODpaJoWF17TXIop7xwtXkiU/edit?usp=sharing"
  const val TMDB_LINK_MAIN = "https://www.themoviedb.org/"


  val tabHomeHeadingArray = intArrayOf(featured, movies, tv_series)
  val tabMoviesTvHeadingArray = intArrayOf( movies, tv_series)
}