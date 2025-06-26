package com.waffiq.bazz_movies.core.user.data.model

import com.waffiq.bazz_movies.core.domain.UserModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserModelTest {

  @Test
  fun userModel_withSomeNullValues_setsPropertiesCorrectly() {
    val user = UserModel(
      userId = 2,
      name = "Jane Doe",
      username = "janedoe",
      password = "",
      region = "US",
      token = "anotherToken",
      isLogin = false,
      gravatarHast = null,
      tmdbAvatar = null
    )

    assertNull(user.gravatarHast)
    assertNull(user.tmdbAvatar)
    assertFalse(user.isLogin)
    assertEquals(2, user.userId)
    assertEquals("Jane Doe", user.name)
    assertEquals("janedoe", user.username)
    assertEquals("", user.password)
  }

  @Test
  fun userModel_withIdenticalProperties_equalsAndHasSameHashCode() {
    val user1 = UserModel(
      userId = 3,
      name = "Alice",
      username = "alice123",
      password = "",
      region = "UK",
      token = "token123",
      isLogin = true,
      gravatarHast = "hash123",
      tmdbAvatar = "https://example.com/avatar2.jpg"
    )

    val user2 = UserModel(
      userId = 3,
      name = "Alice",
      username = "alice123",
      password = "",
      region = "UK",
      token = "token123",
      isLogin = true,
      gravatarHast = "hash123",
      tmdbAvatar = "https://example.com/avatar2.jpg"
    )

    assertEquals(user1, user2)
    assertEquals(user1.hashCode(), user2.hashCode())
  }

  @Test
  fun userModel_whenCopyFunctionCalled_updatesSpecifiedFields() {
    val user = UserModel(
      userId = 4,
      name = "Bob",
      username = "bobby",
      password = "",
      region = "CA",
      token = "copyToken",
      isLogin = false,
      gravatarHast = "copyHash",
      tmdbAvatar = "https://example.com/avatar3.jpg"
    )

    val copiedUser = user.copy(name = "Bob Updated", isLogin = true)

    assertEquals(4, copiedUser.userId)
    assertEquals("Bob Updated", copiedUser.name)
    assertEquals("bobby", copiedUser.username)
    assertTrue(copiedUser.isLogin)
    assertEquals(user.token, copiedUser.token)
  }

  @Test
  fun userModel_withBlankPassword_returnsPasswordIsNotNull() {
    val user = UserModel(
      userId = 5,
      name = "Test User",
      username = "testuser",
      password = "",
      region = "IN",
      token = "securityToken",
      isLogin = true,
      gravatarHast = null,
      tmdbAvatar = null
    )

    assertNotNull(user.password)
    assertTrue(user.password.isBlank())
    assertEquals("", user.password)
  }

  @Test
  fun userModel_withEmptyStrings_setsFieldsToEmpty() {
    // Empty Strings for required fields
    val emptyUser = UserModel(
      userId = 0,
      name = "",
      username = "",
      password = "",
      region = "",
      token = "",
      isLogin = false,
      gravatarHast = null,
      tmdbAvatar = null
    )

    assertEquals(0, emptyUser.userId)
    assertEquals("", emptyUser.name)
    assertEquals("", emptyUser.username)
    assertEquals("", emptyUser.password)
    assertEquals("", emptyUser.region)
    assertEquals("", emptyUser.token)
    assertFalse(emptyUser.isLogin)
  }

  @Test
  fun userModel_withLargeUserId_setsUserIdToMaxValue() {
    val largeIdUser = UserModel(
      userId = Int.MAX_VALUE,
      name = "Max User",
      username = "maxuser",
      password = "",
      region = "MaxRegion",
      token = "maxToken",
      isLogin = true,
      gravatarHast = null,
      tmdbAvatar = null
    )

    assertEquals(Int.MAX_VALUE, largeIdUser.userId)
  }

  @Test
  fun userModel_withSpecialCharacters_setsFieldsCorrectly() {
    val specialCharUser = UserModel(
      userId = 5,
      name = "John@Doe",
      username = "user_name!123",
      password = "pa\$\$word!@#",
      region = "US-West!$",
      token = "tok#en$123",
      isLogin = true,
      gravatarHast = "!hash*special",
      tmdbAvatar = "https://example.com/@vatar?special=chars"
    )

    assertEquals("John@Doe", specialCharUser.name)
    assertEquals("user_name!123", specialCharUser.username)
    assertEquals("pa\$\$word!@#", specialCharUser.password)
    assertEquals("US-West!$", specialCharUser.region)
    assertEquals("tok#en$123", specialCharUser.token)
    assertEquals("!hash*special", specialCharUser.gravatarHast)
    assertEquals("https://example.com/@vatar?special=chars", specialCharUser.tmdbAvatar)
  }

  @Test
  fun userModel_withEmptySpaceValues_setsPropertiesCorrectly() {
    val spaceUser = UserModel(
      userId = 7,
      name = "   ",
      username = "   ",
      password = "   ",
      region = "   ",
      token = "   ",
      isLogin = false,
      gravatarHast = null,
      tmdbAvatar = null
    )

    assertEquals("   ", spaceUser.name)
    assertEquals("   ", spaceUser.username)
    assertEquals("   ", spaceUser.password)
    assertEquals("   ", spaceUser.region)
    assertEquals("   ", spaceUser.token)
  }
}
