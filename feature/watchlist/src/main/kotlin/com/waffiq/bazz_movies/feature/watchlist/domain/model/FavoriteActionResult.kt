package com.waffiq.bazz_movies.feature.watchlist.domain.model

sealed class FavoriteActionResult {
  object Added : FavoriteActionResult()
  object AlreadyInFavorite : FavoriteActionResult()
}
