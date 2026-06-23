package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.authenticationResponseDump
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthenticationResponseTest {

  @Test
  fun authenticationResponse_withValidValues_setsPropertiesCorrectly() {
    val authenticationResponse = authenticationResponseDump
    assertTrue(authenticationResponse.success)
    assertEquals("expire_date", authenticationResponse.expireAt)
    assertEquals("request_token", authenticationResponse.requestToken)
  }

  @Test
  fun authenticationResponse_withDefaultValues_setsPropertiesCorrectly() {
    val authenticationResponse = AuthenticationResponse(true)
    assertNotNull(authenticationResponse.success)
    assertNull(authenticationResponse.expireAt)
    assertNull(authenticationResponse.requestToken)
  }
}
