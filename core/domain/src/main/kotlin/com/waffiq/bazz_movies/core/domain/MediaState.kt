package com.waffiq.bazz_movies.core.domain

data class MediaState(
  val id: Int,
  val favorite: Boolean,
  val rated: Rated,
  val watchlist: Boolean,
)
