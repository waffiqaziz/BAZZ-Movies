package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.models.UserModel

object DummyData {

  val userModel = UserModel(
    userId = 12345678,
    name = "Test Name",
    username = "Test Username",
    password = "",
    region = "id",
    token = "Test Token",
    isLogin = true,
    gravatarHash = "Gravatar Hast",
    tmdbAvatar = "TMDB Avatar",
  )
}
