package com.waffiq.bazz_movies.core.model.user

/**
 * Used as return value from general POST method
 */
data class PostResult(
  val success: Boolean? = null,
  val statusCode: Int? = null,
  val statusMessage: String? = null,
)
