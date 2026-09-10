package com.waffiq.bazz_movies.core.model

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UserModelTest {

  private val userModelValid = UserModel(
    userId = 1,
    name = "John Doe",
    username = "johndoe",
    password = "securePass123",
    region = "US",
    token = "abcd1234token",
    isLogin = true,
    gravatarHash = "123abcHash",
    tmdbAvatar = "/avatar.jpg",
  )

  @Test
  fun userModel_withValidValue_returnsCorrectData() {
    assertEquals(1, userModelValid.userId)
    assertEquals("John Doe", userModelValid.name)
    assertEquals("johndoe", userModelValid.username)
    assertEquals("securePass123", userModelValid.password)
    assertEquals("US", userModelValid.region)
    assertEquals("abcd1234token", userModelValid.token)
    assertTrue(userModelValid.isLogin)
    assertEquals("123abcHash", userModelValid.gravatarHash)
    assertEquals("/avatar.jpg", userModelValid.tmdbAvatar)
  }
}
