package com.waffiq.bazz_movies.core.domain

/**
 * Used for TMDB POST method to favorite
 */
data class FavoriteModel(
  val mediaType: String,
  val mediaId: Int,
  val favorite: Boolean,
)
