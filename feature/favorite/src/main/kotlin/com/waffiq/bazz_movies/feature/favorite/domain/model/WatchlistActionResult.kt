package com.waffiq.bazz_movies.feature.favorite.domain.model

sealed class WatchlistActionResult {
  object Added : WatchlistActionResult()
  object AlreadyInWatchlist : WatchlistActionResult()
}
