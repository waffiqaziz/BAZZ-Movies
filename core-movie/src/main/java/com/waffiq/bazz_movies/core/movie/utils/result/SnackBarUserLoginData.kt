package com.waffiq.bazz_movies.core.movie.utils.result

import com.waffiq.bazz_movies.core.network.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.WatchlistPostModel

data class SnackBarUserLoginData(
  val isSuccess: Boolean,
  val title: String, // also as message error
  val favoritePostModel: FavoritePostModel?,
  val watchlistPostModel: WatchlistPostModel?
)
