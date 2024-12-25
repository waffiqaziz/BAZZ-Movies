package com.waffiq.bazz_movies.core.domain

data class FavoriteModel(
  val mediaType: String,
  val mediaId: Int,
  val favorite: Boolean,
)
