package com.waffiq.bazz_movies.core.utils.result

import com.waffiq.bazz_movies.core.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.WatchlistPostModel

data class SnackBarUserLoginData(
  val isSuccess: Boolean,
  val title: String, // also as message error
  val favoritePostModel: FavoritePostModel?,
  val watchlistPostModel: WatchlistPostModel?
)
