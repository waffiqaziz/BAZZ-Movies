package com.waffiq.bazz_movies.utils.result_state

data class PostModelState(
  val isSuccess: Boolean,
  val isDelete: Boolean,
  val isFavorite: Boolean,
  val isWatchlist: Boolean
)