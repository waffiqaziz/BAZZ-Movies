package com.waffiq.bazz_movies.core.domain

/**
 * Used as return value from general POST method
 */
data class Post(
  val success: Boolean? = null,
  val statusCode: Int? = null,
  val statusMessage: String? = null,
)
