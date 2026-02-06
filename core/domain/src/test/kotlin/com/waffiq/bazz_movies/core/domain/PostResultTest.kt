package com.waffiq.bazz_movies.core.domain

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PostResultTest {

  @Test
  fun postResult_withValidValue_returnsCorrectData() {
    val postResult = PostResult(
      success = true,
      statusCode = 200,
      statusMessage = "OK"
    )

    assertTrue(postResult.success == true)
    assertEquals(200, postResult.statusCode)
    assertEquals("OK", postResult.statusMessage)
  }

  @Test
  fun postResult_withNullValue_returnsCorrectData() {
    val post = PostResult()

    assertNull(post.success)
    assertNull(post.statusCode)
    assertNull(post.statusMessage)
  }
}
