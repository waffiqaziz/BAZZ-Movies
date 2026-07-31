package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.models.UserModel
import com.waffiq.bazz_movies.core.testmodule.MockUserPreferenceViewModelModule.provideMockUserPreferenceViewModel
import com.waffiq.bazz_movies.core.testmodule.MockUserPreferenceViewModelModule.setupLoggedUserModel
import com.waffiq.bazz_movies.core.testmodule.MockUserPreferenceViewModelModule.setupGuestUserModel
import com.waffiq.bazz_movies.core.testmodule.MockUserPreferenceViewModelModule.setupUserModel
import com.waffiq.bazz_movies.core.testmodule.MockUserPreferenceViewModelModule.setupRegion
import org.junit.Assert.assertNotNull
import org.junit.Test

class MockUserPreferenceViewModelModuleTest {

  val userModel = UserModel(
    userId = 1234,
    name = "name",
    username = "username",
    password = "password",
    region = "region",
    token = "token",
    isLogin = true,
    gravatarHash = "gravatarHash",
    tmdbAvatar = "tmdbAvatar",
  )

  @Test
  fun provideMockUserPreferenceViewModel_success_shouldCreateInstance() {
    assertNotNull(provideMockUserPreferenceViewModel())
    setupLoggedUserModel()
    setupGuestUserModel()
    setupUserModel(userModel)
    setupRegion("ID")
  }
}
