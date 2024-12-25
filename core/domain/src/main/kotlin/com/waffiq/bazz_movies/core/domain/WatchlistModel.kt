package com.waffiq.bazz_movies.core.domain

data class WatchlistModel(
  val mediaType: String,
  val mediaId: Int,
  val watchlist: Boolean,
)
