package com.waffiq.bazz_movies.domain.model

import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel

data class SnackBarUserLoginData(
  val isSuccess: Boolean,
  val title: String, // also as message error
  val favoritePostModel: FavoritePostModel?,
  val watchlistPostModel: WatchlistPostModel?
)
