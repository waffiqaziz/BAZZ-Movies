package com.waffiq.bazz_movies.core.user.data.model

data class UserModelPref(
  val userId: Int,
  val name: String,
  val username: String,
  val password: String,
  val region: String,
  val token: String,
  val isLogin: Boolean,
  val gravatarHast: String?,
  val tmdbAvatar: String?
)
