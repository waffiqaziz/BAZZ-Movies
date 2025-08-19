package com.waffiq.bazz_movies.core.domain

/**
 * Used for TMDB POST method to watchlist
 */
data class WatchlistModel(
  override val mediaType: String,
  override val mediaId: Int,
  val watchlist: Boolean,
): MediaData
