package com.waffiq.bazz_movies.data.remote

data class SnackBarLoginData(
  val title: String,
  val favorite: Favorite?,
  val watchlist: Watchlist?,
  val position: Int
)