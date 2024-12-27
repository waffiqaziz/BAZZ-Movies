package com.waffiq.bazz_movies.core.domain

/**
 * Used for TMDB POST method to watchlist
 */
data class WatchlistModel(
  val mediaType: String,
  val mediaId: Int,
  val watchlist: Boolean,
)
