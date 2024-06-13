package com.waffiq.bazz_movies.data.remote

import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel

data class SnackBarLoginData(
  val title: String,
  val favoritePostModel: FavoritePostModel?,
  val watchlistPostModel: WatchlistPostModel?,
  val position: Int
)