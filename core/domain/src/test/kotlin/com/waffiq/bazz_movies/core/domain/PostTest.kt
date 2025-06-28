package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PostTest {

  @Test
  fun post_withValidValue_returnsCorrectData() {
    val post = Post(
      success = true,
      statusCode = 200,
      statusMessage = "OK"
    )

    assertTrue(post.success == true)
    assertEquals(200, post.statusCode)
    assertEquals("OK", post.statusMessage)
  }

  @Test
  fun post_withNullValue_returnsCorrectData() {
    val post = Post()

    assertNull(post.success)
    assertNull(post.statusCode)
    assertNull(post.statusMessage)
  }
}
