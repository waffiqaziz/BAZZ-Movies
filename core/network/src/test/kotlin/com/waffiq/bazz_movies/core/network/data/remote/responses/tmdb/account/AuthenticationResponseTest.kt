package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.authenticationResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
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
