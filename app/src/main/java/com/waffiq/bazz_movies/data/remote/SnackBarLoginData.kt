package com.waffiq.bazz_movies.data.remote

data class SnackBarLoginData(
  val title: String,
  val favoritePostModel: FavoritePostModel?,
  val watchlistPostModel: WatchlistPostModel?,
  val position: Int
)