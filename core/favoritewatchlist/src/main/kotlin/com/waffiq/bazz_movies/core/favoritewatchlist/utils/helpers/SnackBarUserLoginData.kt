package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel

data class SnackBarUserLoginData(
  val isSuccess: Boolean,
  val title: String, // also as message error
  val favoriteModel: FavoriteModel?,
  val watchlistModel: WatchlistModel?
)
