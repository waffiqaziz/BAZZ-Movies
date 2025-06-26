package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PostMapperTest {

  @Test
  fun toPost_withValidTrueValues_returnsPost() {
    val postResponse = PostResponse(true, 200, "Success")

    val post = postResponse.toPost()
    assertTrue(post.success == true)
    assertEquals(200, post.statusCode)
    assertEquals("Success", post.statusMessage)
  }

  @Test
  fun toPost_withValidFalseValues_returnsPost() {
    val postResponse = PostResponse(false, 404, "Not Found")

    val post = postResponse.toPost()
    assertFalse(post.success == true)
    assertEquals(404, post.statusCode)
    assertEquals("Not Found", post.statusMessage)
  }

  @Test
  fun toPost_withNullValues_returnsPost() {
    val postResponse = PostResponse(null, null, null)

    val post = postResponse.toPost()
    assertNull(post.success)
    assertNull(post.statusCode)
    assertNull(post.statusMessage)
  }

  @Test
  fun toPost_withMessageIsEmpty_returnsPost() {
    val postResponse = PostResponse(true, 200, "")

    val post = postResponse.toPost()
    assertEquals(true, post.success)
    assertEquals(200, post.statusCode)
    assertEquals("", post.statusMessage)
  }

  @Test
  fun toPost_withMessageIsBlank_returnsPost() {
    val postResponse = PostResponse(true, 201, "  ")

    val post = postResponse.toPost()
    assertEquals(true, post.success)
    assertEquals(201, post.statusCode)
    assertEquals("  ", post.statusMessage)
  }
}
