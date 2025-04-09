package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class GravatarResponseTest {

  @Test
  fun gravatarResponse_withValidValues_setsPropertiesCorrectly() {
    val gravatarResponse = GravatarResponse(hash = "gravatar_hash")
    assertEquals("gravatar_hash", gravatarResponse.hash)
  }

  @Test
  fun gravatarResponse_withDefaultValues_setsPropertiesCorrectly() {
    val gravatarResponse = GravatarResponse()
    assertNull(gravatarResponse.hash)
  }
}
