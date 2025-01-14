package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class GravatarTest {

  @Test
  fun `test Gravatar creation with all properties set`() {
    val gravatar = Gravatar(hash = "/536978543976.jpg")
    assertEquals("/536978543976.jpg", gravatar.hash)
  }

  @Test
  fun `test Gravatar creation with all properties null`() {
    val gravatar = Gravatar(hash = null)
    assertNull(gravatar.hash)
  }

  @Test
  fun `test Gravatar creation with all default values`() {
    val gravatar = Gravatar()
    assertNull(gravatar.hash)
  }
}
