package com.waffiq.bazz_movies.core.models

/**
 * Used for TMDB POST method to watchlist
 */
data class WatchlistParams(
  override val mediaType: String,
  override val mediaId: Int,
  val watchlist: Boolean,
) : MediaData
