package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.mappers.PostMapper.toPostResult
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PostMapperTest {

  @Test
  fun toPostResult_withValidTrueValues_returnsPostResult() {
    val postResponse = PostResponse(true, 200, "Success")

    val postResult = postResponse.toPostResult()
    assertTrue(postResult.success == true)
    assertEquals(200, postResult.statusCode)
    assertEquals("Success", postResult.statusMessage)
  }

  @Test
  fun toPostResult_withValidFalseValues_returnsPostResult() {
    val postResponse = PostResponse(false, 404, "Not Found")

    val postResult = postResponse.toPostResult()
    assertFalse(postResult.success == true)
    assertEquals(404, postResult.statusCode)
    assertEquals("Not Found", postResult.statusMessage)
  }

  @Test
  fun toPostResult_withNullValues_returnsPostResult() {
    val postResponse = PostResponse(null, null, null)

    val postResult = postResponse.toPostResult()
    assertNull(postResult.success)
    assertNull(postResult.statusCode)
    assertNull(postResult.statusMessage)
  }

  @Test
  fun toPostResult_withMessageIsEmpty_returnsPostResult() {
    val postResponse = PostResponse(true, 200, "")

    val post = postResponse.toPostResult()
    assertEquals(true, post.success)
    assertEquals(200, post.statusCode)
    assertEquals("", post.statusMessage)
  }

  @Test
  fun toPostResult_withMessageIsBlank_returnsPostResult() {
    val postResponse = PostResponse(true, 201, "  ")

    val postResult = postResponse.toPostResult()
    assertEquals(true, postResult.success)
    assertEquals(201, postResult.statusCode)
    assertEquals("  ", postResult.statusMessage)
  }
}
