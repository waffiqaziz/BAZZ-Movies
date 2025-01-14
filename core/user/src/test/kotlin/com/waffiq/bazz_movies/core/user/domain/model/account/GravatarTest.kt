package com.waffiq.bazz_movies.core.user.domain.model.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class GravatarTest {

  @Test
  fun gravatar_WithAllPropertiesSet() {
    val gravatar = Gravatar(hash = "/536978543976.jpg")
    assertEquals("/536978543976.jpg", gravatar.hash)
  }

  @Test
  fun gravatar_WithAllPropertiesNull() {
    val gravatar = Gravatar(hash = null)
    assertNull(gravatar.hash)
  }

  @Test
  fun gravatar_WithDefaultValues() {
    val gravatar = Gravatar()
    assertNull(gravatar.hash)
  }
}
