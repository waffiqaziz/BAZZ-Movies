package com.waffiq.bazz_movies.utils.result_state

/**
 * handler for watchlist and favorite operation on detail page
 */

data class PostModelState(
  val isSuccess: Boolean,
  val isDelete: Boolean,
  val isFavorite: Boolean // parameter as true if it favorite operation and false if watchlist
)