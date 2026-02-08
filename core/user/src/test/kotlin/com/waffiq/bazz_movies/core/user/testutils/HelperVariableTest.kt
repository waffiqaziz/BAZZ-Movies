package com.waffiq.bazz_movies.core.user.testutils

import com.waffiq.bazz_movies.core.user.data.model.UserModelPref

object HelperVariableTest {

  val userModelPref = UserModelPref(
    userId = 123456789,
    name = "John Doe",
    username = "johndoe",
    password = "password123",
    region = "US",
    token = "sampleToken",
    isLogin = true,
    gravatarHash = "hash123",
    tmdbAvatar = "avatar.jpg"
  )
}
