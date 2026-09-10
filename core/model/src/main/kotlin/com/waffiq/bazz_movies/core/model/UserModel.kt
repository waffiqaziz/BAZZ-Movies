package com.waffiq.bazz_movies.core.model

data class UserModel(
  val userId: Int,
  val name: String,
  val username: String,
  val password: String,
  val region: String,
  val token: String,
  val isLogin: Boolean,
  val gravatarHash: String?,
  val tmdbAvatar: String?,
)
