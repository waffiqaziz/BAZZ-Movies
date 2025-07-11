package com.waffiq.bazz_movies.feature.more.testutils

import com.waffiq.bazz_movies.core.domain.UserModel

object Helper {
  val userModel = UserModel(
    userId = 12345678,
    name = "Test Name",
    username = "Test Username",
    password = "",
    region = "id",
    token = "Test Token",
    isLogin = true,
    gravatarHast = "Gravatar Hast",
    tmdbAvatar = "TMDB Avatar"
  )
}
