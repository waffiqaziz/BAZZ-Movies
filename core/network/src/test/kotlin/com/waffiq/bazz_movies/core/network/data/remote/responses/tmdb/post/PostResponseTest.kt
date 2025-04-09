package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.postResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PostResponseTest {

  @Test
  fun postResponse_withValidValues_setsPropertiesCorrectly() {
    val postResponse = postResponseDump
    assertTrue(postResponse.success == true)
    assertEquals(200, postResponse.statusCode)
    assertEquals("Success", postResponse.statusMessage)
  }

  @Test
  fun postResponse_withDefaultValues_setsPropertiesCorrectly() {
    val postResponse = PostResponse()
    assertNull(postResponse.success)
    assertNull(postResponse.statusCode)
    assertNull(postResponse.statusMessage)
  }

  @Test
  fun postResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val postResponse = PostResponse(false, 404)
    assertEquals(false, postResponse.success)
    assertEquals(404, postResponse.statusCode)
    assertNull(postResponse.statusMessage)
  }
}
