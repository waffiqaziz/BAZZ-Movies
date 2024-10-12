package com.waffiq.bazz_movies.core.domain.model.account

data class Authentication(
  val success: Boolean,
  val expireAt: String? = null,
  val requestToken: String? = null
)
