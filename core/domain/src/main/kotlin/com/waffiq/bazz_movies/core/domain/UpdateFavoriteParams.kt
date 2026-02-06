package com.waffiq.bazz_movies.core.domain

/**
 * Used for TMDB POST method to favorite
 */
data class UpdateFavoriteParams(
  override val mediaType: String,
  override val mediaId: Int,
  val favorite: Boolean,
) : MediaData
