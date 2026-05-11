package com.waffiq.bazz_movies.core.models

/**
 * Used for TMDB POST method to favorite
 */
data class FavoriteParams(
  override val mediaType: String,
  override val mediaId: Int,
  val favorite: Boolean,
) : MediaData
